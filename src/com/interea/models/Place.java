package com.interea.models;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.FieldEnd;
import com.google.code.morphia.query.Query;
import com.interea.persistence.MongoFacade;
import com.interea.rest.ObjectIdSerializer;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@Entity
public class Place
{
	@Id
	@JsonSerialize( using = ObjectIdSerializer.class )
	protected ObjectId id;

	@Ref( value = "/rest/places/{id}", style = Style.ABSOLUTE, bindings = { @Binding( name = "id", value = "${instance.id}" ) } )
	@XmlElement
	@Transient
	protected URI href;

	protected String placeName;

	protected String address;

	protected String imageUrl;

	@Indexed( com.google.code.morphia.utils.IndexDirection.GEO2D )
	protected double[ ] loc;

	@Reference
	protected Set<Comment> listOfComments;

	public static Place findById( final String id )
	{
		try
		{
			return MongoFacade.getInstance( ).getDatastore( ).get( Place.class, new ObjectId( id ) );
		}
		catch ( IllegalArgumentException e )
		{
			throw new NotFoundException( "ID '" + id + "' was not found!" );
		}
	}

	public static List<Place> findByLocation( final double longitude, final double latitude, double radius )
	{
		final Datastore ds = MongoFacade.getInstance( ).getDatastore( );
		final Query<Place> query = ds.createQuery( Place.class );
		final FieldEnd<? extends Criteria> criteria = query.criteria( "loc" );

		criteria.within( longitude, latitude, radius / 111.12 );

		return query.asList( );
	}

	public Place( )
	{
		this.listOfComments = new HashSet<Comment>( );
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

	public String getPlaceName( )
	{
		return placeName;
	}

	public void setPlaceName( String placeName )
	{
		this.placeName = placeName;
	}

	public String getAddress( )
	{
		return address;
	}

	public void setAddress( String address )
	{
		this.address = address;
	}

	public String getImageUrl( )
	{
		return imageUrl;
	}

	public void setImageUrl( String imageUrl )
	{
		this.imageUrl = imageUrl;
	}

	public double[ ] getLoc( )
	{
		return this.loc;
	}

	public void setLoc( double[ ] loc )
	{
		this.loc = loc;
	}

	@XmlTransient
	public double getLongitude( )
	{
		return this.loc[ 0 ];
	}

	public void setLongitude( double longitude )
	{
		this.loc[ 0 ] = longitude;
	}

	@XmlTransient
	public double getLatitude( )
	{
		return this.loc[ 1 ];
	}

	public void setLatitude( double latitude )
	{
		this.loc[ 1 ] = latitude;
	}

	@XmlTransient
	public Set<Comment> getListOfComments( )
	{
		return listOfComments;
	}

	public void setListOfComments( Set<Comment> listOfComments )
	{
		this.listOfComments = listOfComments;
	}

	@XmlTransient
	private final Datastore getDataStore( )
	{
		return MongoFacade.getInstance( ).getDatastore( );
	}

}
