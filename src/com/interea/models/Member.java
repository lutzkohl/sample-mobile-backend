package com.interea.models;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.FieldEnd;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.utils.IndexDirection;
import com.interea.persistence.MongoFacade;
import com.interea.rest.ObjectIdSerializer;
import com.interea.rest.PasswordSerializer;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@Entity
public class Member
{
	@Id
	@JsonSerialize( using = ObjectIdSerializer.class )
	protected ObjectId id;

	@Ref( value = "/rest/members/{id}", style = Style.ABSOLUTE, bindings = { @Binding( name = "id", value = "${instance.id}" ) } )
	@XmlElement
	@Transient
	protected URI href;

	@Indexed( value = IndexDirection.ASC, unique = true )
	protected String userName;

	@JsonSerialize( using = PasswordSerializer.class )
	protected String password;

	protected String firstName;

	protected String lastName;

	public Member( )
	{
		super( );
	}

	public static Member findById( final String id )
	{
		try
		{
			return MongoFacade.getInstance( ).getDatastore( ).get( Member.class, new ObjectId( id ) );
		}
		catch ( IllegalArgumentException e )
		{
			throw new NotFoundException( "ID '" + id + "' was not found!" );
		}
	}

	public static Member findByUserName( final String userName )
	{
		final Datastore ds = MongoFacade.getInstance( ).getDatastore( );
		final Query<Member> query = ds.createQuery( Member.class );
		final FieldEnd<? extends Criteria> criteria = query.criteria( "userName" );

		criteria.equal( userName );

		final List<Member> result = query.asList( );

		return result.size( ) == 1 ? result.get( 0 ) : null;
	}

	public void save( )
	{
		getDataStore( ).save( this );
	}

	public ObjectId getId( )
	{
		return id;
	}

	public void setId( ObjectId id )
	{
		this.id = id;
	}

	public String getUserName( )
	{
		return userName;
	}

	public void setUserName( String userName )
	{
		this.userName = userName;
	}

	public String getPassword( )
	{
		return password;
	}

	public void setPassword( String password )
	{
		this.password = password;
	}

	public String getFirstName( )
	{
		return firstName;
	}

	public void setFirstName( String firstName )
	{
		this.firstName = firstName;
	}

	public String getLastName( )
	{
		return lastName;
	}

	public void setLastName( String lastName )
	{
		this.lastName = lastName;
	}

	@XmlTransient
	private final Datastore getDataStore( )
	{
		return MongoFacade.getInstance( ).getDatastore( );
	}

}
