package com.vimal.app.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vimal.app.entity.AttendanceLog;
import com.vimal.app.entity.OvertimeEntry;
import com.vimal.app.entity.Site;
import com.vimal.app.entity.Worker;
import com.vimal.app.enums.SettlementStatus;
import com.vimal.app.exception.AppException;
import com.vimal.app.repository.AttendanceRepository;
import com.vimal.app.repository.OvertimeRepository;
import com.vimal.app.repository.SiteRepository;
import com.vimal.app.repository.WorkerRepository;
import com.vimal.app.request.ActiveWorkerCache;
import com.vimal.app.request.ClockInRequest;
import com.vimal.app.response.PaginationResponse;
import com.vimal.app.service.AttendanceService;
import com.vimal.app.utils.AttendanceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

	private final WorkerRepository workerRepository;

    private final SiteRepository siteRepository;

    private final AttendanceRepository attendanceRepository;

    private final OvertimeRepository overtimeRepository;
    
    private final OvertimeCalculationService overtimeCalculationService;
    
    private final ActiveWorkerCacheService activeWorkerCacheService;

    @Transactional
	@Override
	public void clockIn(ClockInRequest request) {
    	Worker worker = workerRepository.findById(request.workerId())
                        .orElseThrow(()-> new AppException(
                        		"WORKER_NOT_FOUND",
                                "Worker does not exist",
                                HttpStatus.NOT_FOUND
                        ));

        if (!worker.isActive()) {
            throw new AppException(
            		"WORKER_INACTIVE",
                    "Worker is inactive",
                    HttpStatus.BAD_REQUEST
            );
        }

        Site site = siteRepository.findById(request.siteId())
                        .orElseThrow(()-> new AppException(
                        		"SITE_NOT_FOUND",
                                "Site does not exist",
                                HttpStatus.NOT_FOUND
                        ));

        if (!site.isActive()) {
            throw new AppException(
            		"SITE_INACTIVE",
                    "Site is inactive",
                    HttpStatus.BAD_REQUEST
            );
        }

        attendanceRepository
                .findByWorkerIdAndActiveTrue(worker.getId())
                .ifPresent(a -> {
                    throw new AppException(
                    		"DUPLICATE_CLOCK_IN",
                            "Worker is already clocked in at Site: " + a.getSite().getName(),
                            HttpStatus.CONFLICT
                    );
                });

        AttendanceLog attendance = AttendanceLog.builder()
                        .worker(worker)
                        .site(site)
                        .clockIn(LocalDateTime.now())
                        .active(true)
                        .build();

        attendanceRepository.save(attendance);

        try {
        	activeWorkerCacheService.save(
                    new ActiveWorkerCache(
                            worker.getId(),
                            worker.getName(),
                            worker.getDesignation().name(),
                            site.getId(),
                            site.getName(),
                            attendance.getClockIn()
                    )
            );

        } catch (Exception ex) {
            log.warn("Redis unavailable: {}", ex.getMessage());
        }
    }

    @Transactional
	@Override
	public void clockOut(Long workerId) {
    	AttendanceLog attendance =
                attendanceRepository
                        .findByWorkerIdAndActiveTrue(workerId)
                        .orElseThrow(()-> new AppException(
                        		"CLOCK_IN_NOT_FOUND",
                                "Worker is not currently clocked in",
                                HttpStatus.NOT_FOUND
                        	));

        LocalDateTime out = LocalDateTime.now();

        attendance.setClockOut(out);

        attendance.setActive(false);

        BigDecimal totalHours =
                BigDecimal.valueOf(
                        Duration.between(
                                attendance.getClockIn(),
                                out
                        ).toMinutes() / 60.0
                );

        attendance.setTotalHoursWorked(totalHours);

        if (totalHours.compareTo(BigDecimal.valueOf(16)) > 0) {
            attendance.setFlagged(true);
        }

        BigDecimal overtime = totalHours.subtract(BigDecimal.valueOf(8));

        if (overtime.compareTo(BigDecimal.ZERO) > 0) {

            overtime = overtimeCalculationService.applyMonthlyCap(workerId, YearMonth.now(), overtime);

            attendance.setOvertimeHours(overtime);

            BigDecimal amount =
            		overtimeCalculationService.calculateOvertimeAmount(overtime, attendance.getWorker().getDailyWageRate());

            OvertimeEntry entry =
                    OvertimeEntry.builder()
                            .worker(attendance.getWorker())
                            .attendance(attendance)
                            .date(LocalDate.now())
                            .overtimeHours(overtime)
                            .overtimeRateApplied(
                                    attendance
                                            .getWorker()
                                            .getDailyWageRate()
                            )
                            .amount(amount)
                            .settlementStatus(
                                    SettlementStatus.PENDING
                            )
                            .build();

            overtimeRepository.save(entry);
        }

        attendanceRepository.save(attendance);

        try {
        	activeWorkerCacheService.remove(workerId);
        } catch (Exception ex) {
            log.warn("Redis unavailable: {}", ex.getMessage());
        }
	}

    @Override
	public List<ActiveWorkerCache> getActiveWorkers() {
		return activeWorkerCacheService.getAllActiveWorkers();
	}
    
	@Override
	public PaginationResponse getLogs(Long workerId, LocalDate from, LocalDate to, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

	    Page<AttendanceLog> logs =
	            attendanceRepository
	                    .findByWorkerIdAndClockInBetween(
	                            workerId,
	                            from.atStartOfDay(),
	                            to.atTime(LocalTime.MAX),
	                            pageable
	                    );

	    return new PaginationResponse(
	            logs.getContent()
	                    .stream()
	                    .map(AttendanceMapper::attendanceToDto)
	                    .toList(),

	            logs.getTotalElements(),

	            logs.getTotalPages(),

	            logs.getNumber(),

	            logs.getSize()
	    );
	}

}
