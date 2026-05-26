package com.vimal.app.service;

import java.math.BigDecimal;

public interface SMSService {
	void sendSettlementSms(String phone, BigDecimal amount);
}
