package com.utils.jdbc.data_sources;

import java.io.InputStream;
import java.util.Properties;

import com.utils.annotations.ApiMethod;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;

public final class FactoryDataSourcePostgresql {

	private FactoryDataSourcePostgresql() {
	}

	@ApiMethod
	public static DataSourcePostgresql newInstance(
			final String propertiesFilePathString) {

		final Properties properties = createProperties(propertiesFilePathString);
		final String databaseUrl = properties.getProperty("databaseUrl");
		return new DataSourcePostgresql(databaseUrl, properties);
	}

	private static Properties createProperties(
			final String propertiesFilePathString) {

		final Properties properties = new Properties();
		Logger.printProgress("loading postgresql DB properties from:");
		Logger.printLine(propertiesFilePathString);
		try (InputStream inputStream = StreamUtils.openBufferedInputStream(propertiesFilePathString)) {
			properties.load(inputStream);

		} catch (final Throwable throwable) {
			Logger.printError("failed to load postgresql DB properties");
			Logger.printThrowable(throwable);
		}
		return properties;
	}
}
