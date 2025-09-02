package com.utils.jdbc;

import com.utils.jdbc.data_sources.DataSourcePostgresql;

public abstract class AbstractDatabaseConnectionPostgresql extends AbstractDatabaseConnection {

	public AbstractDatabaseConnectionPostgresql(
			final DataSourcePostgresql dataSourcePostgresql) {

		super(dataSourcePostgresql);
	}
}
