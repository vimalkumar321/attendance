package com.vimal.app.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vimal.app.entity.OvertimeEntry;
import com.vimal.app.enums.SettlementStatus;

public interface OvertimeRepository extends JpaRepository<OvertimeEntry, Long>{
	@Query("""
	        SELECT COALESCE(SUM(o.overtimeHours), 0)
	        FROM OvertimeEntry o
	        WHERE o.worker.id = :workerId
	        AND o.date BETWEEN :startDate AND :endDate
	    """)
	Optional<BigDecimal> sumMonthlyOvertimeHours(
			@Param("workerId") Long workerId,
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate
	);

	List<OvertimeEntry> findByWorkerIdAndDateBetweenAndSettlementStatus(
            Long workerId,
            LocalDate start,
            LocalDate end,
            SettlementStatus status
    );
	
	@Query("""
		       SELECT COALESCE(SUM(a.overtimeHours), 0)
		       FROM AttendanceLog a
		       WHERE a.worker.id = :workerId
		       AND YEAR(a.clockIn) = :year
		       AND MONTH(a.clockIn) = :month
		    """)
	BigDecimal getMonthlyOvertime(Long workerId, int year, int month);

	List<OvertimeEntry> findByWorkerIdAndDateBetween(Long workerId, LocalDate start, LocalDate end);
}
