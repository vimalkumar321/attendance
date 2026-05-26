package com.vimal.app.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.vimal.app.request.OvertimeSettledEvent;
import com.vimal.app.service.SMSService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OvertimeSettlementListener {
	
	private final SMSService smsService;
	
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OvertimeSettledEvent event) {
        try {
        	log.info("Processing overtime settlement notification for workerId={}, amount={}", event.workerId(), event.amount());
            
        	smsService.sendSettlementSms(event.phone(), event.amount());
        	
            log.info("Settlement notification sent successfully to {}", event.phone());
        } catch (Exception ex) {
        	log.error(
                    "Failed to send overtime settlement SMS for workerId={} : {}",
                    event.workerId(),
                    ex.getMessage(),
                    ex
            );
        }
    }
}
