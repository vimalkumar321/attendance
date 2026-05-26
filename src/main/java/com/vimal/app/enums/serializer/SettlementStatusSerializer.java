package com.vimal.app.enums.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vimal.app.enums.SettlementStatus;

public class SettlementStatusSerializer extends JsonSerializer<SettlementStatus>{

	@Override
	public void serialize(SettlementStatus status, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(status.getText());
	}

}
