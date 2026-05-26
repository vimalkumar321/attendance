package com.vimal.app.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vimal.app.request.ClockInRequest;
import com.vimal.app.request.ClockOutRequest;
import com.vimal.app.response.PaginationResponse;
import com.vimal.app.service.AttendanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("attendance/")
@RequiredArgsConstructor
public class AttendanceController {
	private final AttendanceService attendanceService;
	
	@PostMapping("clock-in")
    public ResponseEntity<String> clockIn(@Valid @RequestBody ClockInRequest request) {
        attendanceService.clockIn(request);
        return ResponseEntity.ok("Clock in successful");
    }

    @PostMapping("clock-out")
    public ResponseEntity<String> clockOut(@Valid @RequestBody ClockOutRequest request) {
        attendanceService.clockOut(request.workerId());
        return ResponseEntity.ok("Clock out successful");
    }

    @GetMapping("active")
    public Object getActiveWorkers() {
        return attendanceService.getActiveWorkers();
    }

    @GetMapping("log/{workerId}")
    public ResponseEntity<PaginationResponse> getLogs(
            @PathVariable
            Long workerId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                attendanceService.getLogs(workerId, from, to, page, size)
        );
    }
}
