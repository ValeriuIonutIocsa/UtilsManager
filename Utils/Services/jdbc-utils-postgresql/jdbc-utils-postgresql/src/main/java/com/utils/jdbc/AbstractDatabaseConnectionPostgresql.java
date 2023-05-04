package com.utils.jdbc;

import com.utils.jdbc.data_sources.FactoryDataSourcePostgresql;

public abstract class AbstractDatabaseConnectionPostgresql extends AbstractDatabaseConnection {

	public AbstractDatabaseConnectionPostgresql() {

		super(FactoryDataSourcePostgresql.getInstance());
	}
}
