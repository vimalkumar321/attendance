package com.vimal.app.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OvertimeBreakdownResponse(
		LocalDate date,

        BigDecimal overtimeHours,

        BigDecimal overtimeAmount,

        String settlementStatus) {

}
