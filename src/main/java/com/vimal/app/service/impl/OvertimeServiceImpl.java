package com.vimal.app.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vimal.app.entity.OvertimeEntry;
import com.vimal.app.enums.SettlementStatus;
import com.vimal.app.exception.AppException;
import com.vimal.app.repository.OvertimeRepository;
import com.vimal.app.request.OvertimeSettledEvent;
import com.vimal.app.response.OvertimeBreakdownResponse;
import com.vimal.app.response.OvertimeSummaryResponse;
import com.vimal.app.response.SettlementResponse;
import com.vimal.app.service.OvertimeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OvertimeServiceImpl implements OvertimeService {
	
	private final ApplicationEventPublisher publisher;
	private final OvertimeRepository overtimeRepository;

	@Override
	@Transactional(readOnly = true)
	public OvertimeSummaryResponse getSummary(Long workerId, YearMonth month) {
		List<OvertimeEntry> entries =
	            overtimeRepository.findByWorkerIdAndDateBetween(
	                    workerId,
	                    month.atDay(1),
	                    month.atEndOfMonth()
	            );

	    if (entries.isEmpty()) {
	        throw new AppException(
	        		"NO_PENDING_OVERTIME",
	                "No pending overtime entries found",
	                HttpStatus.NOT_FOUND
	        );
	    }

	    BigDecimal totalHours =
	            entries.stream()
	                    .map(OvertimeEntry::getOvertimeHours)
	                    .reduce(
	                            BigDecimal.ZERO,
	                            BigDecimal::add
	                    );

	    BigDecimal totalAmount =
	            entries.stream()
	                    .map(OvertimeEntry::getAmount)
	                    .reduce(
	                            BigDecimal.ZERO,
	                            BigDecimal::add
	                    );

	    String settlementStatus =
	            entries.stream()
	                    .allMatch(e ->
	                            e.getSettlementStatus()
	                                    == SettlementStatus.SETTLED
	                    )
	                    ? "SETTLED"
	                    : "PENDING";

	    List<OvertimeBreakdownResponse> breakdown =
	            entries.stream()
	                    .map(entry ->
	                            new OvertimeBreakdownResponse(
	                                    entry.getDate(),
	                                    entry.getOvertimeHours(),
	                                    entry.getAmount(),
	                                    entry.getSettlementStatus()
	                                            .name()
	                            )
	                    )
	                    .toList();

	    return new OvertimeSummaryResponse(
	            workerId,
	            entries.get(0)
	                    .getWorker()
	                    .getName(),
	            month.toString(),
	            totalHours,
	            totalAmount,
	            settlementStatus,
	            breakdown
	    );
	}
	
	@Transactional
	@Override
	public SettlementResponse settle(Long workerId, YearMonth month) {
		
		if (month.equals(YearMonth.now())) {
	        throw new AppException(
	        		"CURRENT_MONTH_SETTLEMENT_NOT_ALLOWED",
	                "Cannot settle current month",
	                HttpStatus.CONFLICT
	        );
	    }
		
		List<OvertimeEntry> entries = overtimeRepository.findByWorkerIdAndDateBetweenAndSettlementStatus(
			workerId,
            month.atDay(1),
            month.atEndOfMonth(),
            SettlementStatus.PENDING
           );

		if (entries.isEmpty()) {
			throw new AppException(
					"NO_PENDING_OVERTIME",
		            "No pending overtime entries found",
		            HttpStatus.NOT_FOUND
			);
		}
    
		BigDecimal total =
				entries.stream()
                    .map(OvertimeEntry::getAmount)
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    );

		entries.forEach(entry ->
            	entry.setSettlementStatus(
            			SettlementStatus.SETTLED
            	)
		);

		overtimeRepository.saveAll(entries);

		publisher.publishEvent(
				new OvertimeSettledEvent(
						workerId,
						entries.get(0)
                            .getWorker()
                            .getPhone(),
                            total
				)
		);

		return new SettlementResponse(total, "SETTLED", Instant.now());
	}
	
}
