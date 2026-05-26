package com.vimal.app.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vimal.app.enums.deserializer.SettlementStatusDeserializer;
import com.vimal.app.enums.serializer.SettlementStatusSerializer;

@JsonSerialize(using = SettlementStatusSerializer.class)
@JsonDeserialize(using = SettlementStatusDeserializer.class)
public enum SettlementStatus {
	
	PENDING("Pending"),
    SETTLED("Settled");
    
	private final String text;
	
	SettlementStatus(final String text){
		this.text = text;
	}
	
	@Override
    public String toString() {
        return  text;
    }

    public String getText() {
        return text;
    }
    
    public static SettlementStatus fromText(String text){
        for (SettlementStatus status : SettlementStatus.values()) {
            if (status.getText().equals(text)) {
                return status;
            }
        }
        return null;
    }
}
