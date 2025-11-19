package com.utils.jdbc.data_sources;

import java.util.Properties;

public class DataSourcePostgresql extends AbstractDataSource {

	public DataSourcePostgresql(
			final String databaseUrl,
			final Properties properties) {

		super("jdbc:postgresql", databaseUrl, properties);
	}

	@Override
	boolean loadDriver() {
		return true;
	}
}
