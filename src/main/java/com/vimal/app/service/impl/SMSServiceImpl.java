package com.vimal.app.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.vimal.app.service.SMSService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SMSServiceImpl implements SMSService {

	@Override
	public void sendSettlementSms(String phone, BigDecimal amount) {
		try {

            String message =
                    """
                    Dear Worker,
                    Your overtime payment of ₹%s
                    has been settled successfully.
                    """
                            .formatted(amount);

            /*
             * Integrate SMS Provider API here
             *
             * Example:
             *
             * twilioService.sendSms(phone, message);
             */

            log.info("SMS sent successfully to {} : {}", phone, message);

        } catch (Exception ex) {
            log.error("Failed to send SMS to {} : {}", phone, ex.getMessage(), ex);
            throw ex;
        }
	}

}
