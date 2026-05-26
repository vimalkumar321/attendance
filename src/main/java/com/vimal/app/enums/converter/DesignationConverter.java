package com.vimal.app.enums.converter;

import com.vimal.app.enums.Designation;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DesignationConverter implements AttributeConverter<Designation, String>{

	@Override
	public String convertToDatabaseColumn(Designation attribute) {
		if(attribute != null){
            return attribute.getText();
        }
        return null;
//        return attribute != null ? attribute.getText() : null;
	}

	@Override
	public Designation convertToEntityAttribute(String dbData) {
		return Designation.fromText(dbData);
	}

}
