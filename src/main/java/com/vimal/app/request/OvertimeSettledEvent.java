package com.vimal.app.request;

import java.math.BigDecimal;

public record OvertimeSettledEvent(
		Long workerId,

        String phone,

        BigDecimal amount) {
	
}
