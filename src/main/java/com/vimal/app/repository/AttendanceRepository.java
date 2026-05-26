package com.vimal.app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vimal.app.entity.AttendanceLog;

public interface AttendanceRepository extends JpaRepository<AttendanceLog, Long>{
	
	Optional<AttendanceLog> findByWorkerIdAndActiveTrue(Long workerId);
	
	@EntityGraph(attributePaths = { "worker", "site" })
    Page<AttendanceLog> findByWorkerIdAndClockInBetween(
            Long workerId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
	
}
