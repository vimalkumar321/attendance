package com.vimal.app.service;

import java.time.YearMonth;

import com.vimal.app.response.OvertimeSummaryResponse;
import com.vimal.app.response.SettlementResponse;

public interface OvertimeService {
	OvertimeSummaryResponse getSummary(Long workerId, YearMonth month);
	SettlementResponse settle(Long workerId, YearMonth month);
}
