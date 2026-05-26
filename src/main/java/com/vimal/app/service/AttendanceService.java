package com.vimal.app.service;

import java.time.LocalDate;
import java.util.List;

import com.vimal.app.request.ActiveWorkerCache;
import com.vimal.app.request.ClockInRequest;
import com.vimal.app.response.PaginationResponse;

public interface AttendanceService {
	void clockIn(ClockInRequest request);
	void clockOut(Long workerId);
	List<ActiveWorkerCache> getActiveWorkers();
	PaginationResponse getLogs(Long workerId, LocalDate from, LocalDate to, int page, int size);
}
