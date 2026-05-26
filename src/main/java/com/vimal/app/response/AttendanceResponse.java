package com.vimal.app.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AttendanceResponse(
		Long id,

        String workerName,

        String siteName,

        LocalDateTime clockIn,

        LocalDateTime clockOut,

        BigDecimal totalHoursWorked,

        BigDecimal overtimeHours) {

}
