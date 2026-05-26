package com.vimal.app.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vimal.app.enums.deserializer.DesignationDeserializer;
import com.vimal.app.enums.serializer.DesignationSerializer;

@JsonSerialize(using = DesignationSerializer.class)
@JsonDeserialize(using = DesignationDeserializer.class)
public enum Designation {
	
	MASON("Mason"),
    ELECTRICIAN("Electrician"),
    PLUMBER("Plumber"),
    SUPERVISOR("Supervisor"),
    HELPER("Helper");
	
    private final String text;
    
    Designation(final String text) {
    	this.text = text;
    }
    @Override
    public String toString() {
        return  text;
    }

    public String getText() {
        return text;
    }
    
    public static Designation fromText(String text){
        for (Designation designation : Designation.values()) {
            if (designation.getText().equals(text)) {
                return designation;
            }
        }
        return null;
    }
}
