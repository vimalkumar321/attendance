package com.vimal.app.response;

import java.math.BigDecimal;
import java.util.List;

public record OvertimeSummaryResponse(
		Long workerId,

        String workerName,

        String month,

        BigDecimal totalOvertimeHours,

        BigDecimal totalPayoutAmount,

        String settlementStatus,

        List<OvertimeBreakdownResponse> breakdown) {

}
