package com.utils.jdbc;

import com.utils.jdbc.data_sources.FactoryDataSourcePostgresql;

public abstract class DatabaseConnectionPostgresql extends DatabaseConnection {

	public DatabaseConnectionPostgresql() {

		super(FactoryDataSourcePostgresql.getInstance());
	}
}
