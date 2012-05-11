package com.interea.persistence;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class MongoFacade
{
	public static final String MAIN_DATABASE_NAME = "interea";

	private static Map<ClassLoader, MongoFacade> instances = new HashMap<ClassLoader, MongoFacade>( );

	public static MongoFacade getInstance( )
	{
		final ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
		if ( instances.get( cl ) == null )
		{
			instances.put( cl, new MongoFacade( ) );
		}

		return instances.get( cl );
	}

	private Mongo mongo;

	private Morphia morphia;

	private MongoFacade( )
	{
		try
		{
			this.mongo = new Mongo( );
			this.morphia = new Morphia( );
		}
		catch ( Exception e )
		{
			this.mongo = null;
			e.printStackTrace( );
		}
	}

	public Datastore getDatastore( )
	{
		return getDatastore( MAIN_DATABASE_NAME );
	}

	public static void dropDatastore( final String applicationName )
	{
		if ( applicationName.equalsIgnoreCase( MAIN_DATABASE_NAME ) )
		{
			throw new IllegalArgumentException( "Dropping main database not allowed" );
		}
		getInstance( ).mongo.dropDatabase( applicationName );
	}

	private Datastore getDatastore( final String applicationName )
	{
		final Datastore ds = this.morphia.createDatastore( this.mongo, applicationName );

		ds.ensureIndexes( );
		return ds;
	}
}
