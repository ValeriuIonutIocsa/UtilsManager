package com.utils.jdbc.data_sources;

import java.io.InputStream;
import java.util.Properties;

import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;

public final class FactoryDataSourcePostgresql {

	private static DataSourcePostgresql instance = newInstance();

	private FactoryDataSourcePostgresql() {
	}

	private static DataSourcePostgresql newInstance() {

		final Properties properties = createProperties();
		final String databaseUrl = properties.getProperty("databaseUrl");
		return new DataSourcePostgresql(databaseUrl, properties);
	}

	private static Properties createProperties() {

		final Properties properties = new Properties();
		final String propertiesFilePathString = PathUtils.computePath(
				PathUtils.createRootPath(), "IVI_MISC", "Tmp", "_cnf", "postgresql_db.properties");
		Logger.printProgress("loading postgresql DB properties from:");
		Logger.printLine(propertiesFilePathString);
		try (InputStream inputStream = StreamUtils.openBufferedInputStream(propertiesFilePathString)) {
			properties.load(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to load postgresql DB properties");
			Logger.printException(exc);
		}
		return properties;
	}

	public static void setInstance(
			final DataSourcePostgresql instance) {
		FactoryDataSourcePostgresql.instance = instance;
	}

	public static DataSourcePostgresql getInstance() {
		return instance;
	}
}
