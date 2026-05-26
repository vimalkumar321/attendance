package com.vimal.app.response;

import java.math.BigDecimal;
import java.time.Instant;

public record SettlementResponse(
		BigDecimal totalAmount,

        String status,

        Instant settledAt) {

}
