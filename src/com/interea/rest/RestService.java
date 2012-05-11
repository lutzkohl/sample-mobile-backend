package com.interea.rest;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;

import com.interea.models.Comment;
import com.interea.models.Member;
import com.interea.models.Place;

@Path( "/rest" )
@Produces( { MediaType.APPLICATION_JSON } )
public class RestService
{
	private static final String HEADER_BASIC = "Basic ";

	private static final String HEADER_AUTHORIZATION = "Authorization";

	@Context
	private UriInfo uri;

	@Context
	private HttpServletRequest request;

	@POST
	@Path( "/members" )
	public Response createNewMember( final Member member )
	{
		member.save( );

		final URI uri = this.uri.getAbsolutePathBuilder( ).path( member.getId( ).toString( ) ).build( );
		return Response.created( uri ).build( );
	}

	@GET
	@Path( "/members/{id}" )
	public Member getMemberById( @PathParam( "id" ) String id )
	{
		return Member.findById( id );
	}

	@GET
	@Path( "/members/me" )
	public Member getMemberDataModelOfRequestingUser( )
	{
		return authenticateUser( );
	}

	@POST
	@Path( "/places" )
	public Response createNewPlace( final Place place )
	{
		place.save( );

		final URI uri = this.uri.getAbsolutePathBuilder( ).path( place.getId( ).toString( ) ).build( );
		return Response.created( uri ).build( );
	}

	@GET
	@Path( "/places" )
	public List<Place> findPlacesByLocation( @QueryParam( "lng" ) final double longitude,
		@QueryParam( "lat" ) final double latitude,
		@DefaultValue( "1000.0" ) @QueryParam( "radius" ) final double radius )
	{
		return Place.findByLocation( longitude, latitude, radius );
	}

	@POST
	@Path( "/places/{placeId}/comments" )
	public Response createNewComment( @PathParam( "placeId" ) String placeId, final Comment comment )
	{
		final Place place = Place.findById( placeId );
		final Member author = authenticateUser( );

		comment.setAuthor( author );
		comment.setPlace( place );
		comment.save( );

		place.getListOfComments( ).add( comment );
		place.save( );

		final URI uri = this.uri.getAbsolutePathBuilder( ).path( comment.getId( ).toString( ) ).build( );
		return Response.created( uri ).build( );
	}

	@GET
	@Path( "/places/{placeId}/comments" )
	public Set<Comment> findAllComments( @PathParam( "placeId" ) final String placeId )
	{
		return Place.findById( placeId ).getListOfComments( );
	}

	private Member authenticateUser( )
	{
		final String header = this.request.getHeader( HEADER_AUTHORIZATION );

		if ( header != null && header.isEmpty( ) == false )
		{
			final String cutBASIC = header.substring( HEADER_BASIC.length( ) );
			final byte[ ] decoded = Base64.decodeBase64( cutBASIC );
			final String[ ] parts = new String( decoded ).split( ":" );

			if ( parts.length == 2 )
			{
				final String userName = parts[ 0 ];
				final String password = parts[ 1 ];

				final Member member = Member.findByUserName( userName );

				if ( member != null )
				{
					if ( member.getPassword( ).equals( password ) )
					{
						return member;
					}
				}
			}
		}

		throw new UnauthorizedException( );
	}
}
