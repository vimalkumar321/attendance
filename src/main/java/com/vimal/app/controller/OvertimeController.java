package com.vimal.app.controller;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vimal.app.response.OvertimeSummaryResponse;
import com.vimal.app.response.SettlementResponse;
import com.vimal.app.service.OvertimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("overtime/")
@RequiredArgsConstructor
public class OvertimeController {
	private final OvertimeService overtimeService;

    @PostMapping("settle/{workerId}")
    public ResponseEntity<SettlementResponse> settleOvertime(
            @PathVariable
            Long workerId,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth month
    ) {

        return ResponseEntity.ok(
                overtimeService.settle(workerId, month)
        );
    }
    
    @GetMapping("summary/{workerId}")
    public OvertimeSummaryResponse getSummary(
    		@PathVariable
            Long workerId,
            
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth month

    ) {
        return overtimeService.getSummary(workerId, month);
    }

}
