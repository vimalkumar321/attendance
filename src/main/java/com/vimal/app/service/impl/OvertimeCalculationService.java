package com.vimal.app.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.vimal.app.repository.OvertimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OvertimeCalculationService {
	private static final BigDecimal STANDARD_SHIFT_HOURS = BigDecimal.valueOf(8);

    private static final BigDecimal MONTHLY_OVERTIME_CAP = BigDecimal.valueOf(60);

    private final OvertimeRepository overtimeRepository;

    /**
     * Applies monthly overtime cap (60 hours).
     */
    public BigDecimal applyMonthlyCap(Long workerId, YearMonth month, BigDecimal requestedOvertime) {

        BigDecimal usedHours =
                overtimeRepository.sumMonthlyOvertimeHours(
                                workerId,
                                month.atDay(1),
                                month.atEndOfMonth()
                        )
                        .orElse(BigDecimal.ZERO);

        BigDecimal remaining = MONTHLY_OVERTIME_CAP.subtract(usedHours);

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return requestedOvertime.min(remaining);
    }

    /**
     * Calculates overtime payout amount.
     *
     * Rules:
     * First 2 OT hours -> 1.5x
     * Beyond 2 hours -> 2x
     */
    public BigDecimal calculateOvertimeAmount(BigDecimal overtimeHours, BigDecimal dailyWage) {
        if (overtimeHours.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal hourlyRate =
                dailyWage.divide(
                        STANDARD_SHIFT_HOURS,
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal firstTwoHours = overtimeHours.min(BigDecimal.valueOf(2));

        BigDecimal remainingHours = overtimeHours.subtract(firstTwoHours).max(BigDecimal.ZERO);

        BigDecimal firstAmount =
                firstTwoHours
                        .multiply(hourlyRate)
                        .multiply(BigDecimal.valueOf(1.5));

        BigDecimal remainingAmount =
                remainingHours
                        .multiply(hourlyRate)
                        .multiply(BigDecimal.valueOf(2));

        return firstAmount.add(remainingAmount);
    }
}
