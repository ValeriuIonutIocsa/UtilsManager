package com.utils.jdbc.data_sources;

import java.util.Properties;

public class DataSourceOracleDb extends AbstractDataSource {

	public DataSourceOracleDb(
			final String databaseUrl,
			final String tempFolderPathString,
			final Properties properties) {

		super("jdbc:oracle:thin", databaseUrl, tempFolderPathString, properties);
	}

	@Override
	boolean loadDriver() {
		return true;
	}
}
