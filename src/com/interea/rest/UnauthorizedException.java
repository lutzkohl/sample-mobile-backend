package com.interea.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

public class UnauthorizedException extends WebApplicationException
{
	private static final long serialVersionUID = -2441365115575718436L;

	public UnauthorizedException( )
	{
		this( "Authentication failed!" );
	}

	public UnauthorizedException( String message )
	{
		super( Response.status( Status.UNAUTHORIZED ).entity( message ).build( ) );
	}
}
