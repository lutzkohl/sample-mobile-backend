package com.interea.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class PasswordSerializer extends JsonSerializer<String>
{
	@Override
	public void serialize( String password, JsonGenerator jsong, SerializerProvider sprovider ) throws IOException,
		JsonProcessingException
	{
		jsong.writeString( "" );
	}

}