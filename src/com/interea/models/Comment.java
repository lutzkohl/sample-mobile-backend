package com.interea.models;

import java.net.URI;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.interea.persistence.MongoFacade;
import com.interea.rest.ObjectIdSerializer;
import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@Entity
public class Comment
{
	@Id
	@JsonSerialize( using = ObjectIdSerializer.class )
	protected ObjectId id;

	@Ref( value = "/rest/places/{placeId}/comments/{commentId}", style = Style.ABSOLUTE, bindings = {
		@Binding( name = "commentId", value = "${instance.id}" ),
		@Binding( name = "placeId", value = "${instance.place.id}" ) } )
	@XmlElement
	@Transient
	protected URI href;

	protected Date createdAt;

	protected String text;

	@Reference
	protected Place place;

	@Reference
	protected Member author;

	@Ref( value = "/rest/members/{id}", style = Style.ABSOLUTE, bindings = { @Binding( name = "id", value = "${instance.author.id}" ) } )
	@XmlElement
	@Transient
	protected URI authorHref;

	public Comment( )
	{
		this.createdAt = new Date( );
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

	public Date getCreatedAt( )
	{
		return createdAt;
	}

	public void setCreatedAt( Date createdAt )
	{
		this.createdAt = createdAt;
	}

	public String getText( )
	{
		return text;
	}

	public void setText( String text )
	{
		this.text = text;
	}

	@XmlTransient
	public Member getAuthor( )
	{
		return author;
	}

	public void setAuthor( Member author )
	{
		this.author = author;
	}

	@XmlTransient
	public Place getPlace( )
	{
		return place;
	}

	public void setPlace( Place place )
	{
		this.place = place;
	}

	@XmlTransient
	private final Datastore getDataStore( )
	{
		return MongoFacade.getInstance( ).getDatastore( );
	}

}
