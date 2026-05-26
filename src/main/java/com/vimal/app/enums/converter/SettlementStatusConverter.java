package com.vimal.app.enums.converter;

import com.vimal.app.enums.SettlementStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SettlementStatusConverter implements AttributeConverter<SettlementStatus, String>{

	@Override
	public String convertToDatabaseColumn(SettlementStatus attribute) {
//		if(attribute != null){
//            return attribute.getText();
//        }
		return attribute != null ? attribute.getText() : null;
	}

	@Override
	public SettlementStatus convertToEntityAttribute(String dbData) {
		return SettlementStatus.fromText(dbData);
	}

}
