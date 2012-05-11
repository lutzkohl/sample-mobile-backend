package com.interea.rest;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ObjectIdSerializer extends JsonSerializer<ObjectId>
{
	@Override
	public void serialize( ObjectId objectId, JsonGenerator jsong, SerializerProvider sprovider ) throws IOException,
		JsonProcessingException
	{
		jsong.writeString( objectId.toString( ) );
	}

}
