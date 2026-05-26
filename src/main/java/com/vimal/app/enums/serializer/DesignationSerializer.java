package com.vimal.app.enums.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vimal.app.enums.Designation;

public class DesignationSerializer extends JsonSerializer<Designation>{

	@Override
	public void serialize(Designation value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.getText());
	}

}
