package com.utils.jdbc.data_sources;

import java.util.Properties;

import com.utils.log.Logger;

public class DataSourceSqlLite extends AbstractDataSource {

	public DataSourceSqlLite(
			final String databaseUrl,
			final String tempFolderPathString,
			final Properties properties) {

		super("jdbc:sqlite", databaseUrl, tempFolderPathString, properties);
	}

	@Override
	boolean loadDriver() {

		boolean success = false;
		try {
			Class.forName("org.sqlite.JDBC");
			success = true;

		} catch (final Throwable throwable) {
			Logger.printError("failed to load SQLLite driver");
		}
		return success;
	}
}
