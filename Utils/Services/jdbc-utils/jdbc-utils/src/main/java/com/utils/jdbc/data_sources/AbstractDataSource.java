package com.utils.jdbc.data_sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.env.EnvProviderSystem;

abstract class AbstractDataSource implements DataSource {

	private final String driver;
	private final String databaseUrl;
	private final Properties properties;

	AbstractDataSource(
			final String driver,
			final String databaseUrl,
			final Properties properties) {

		this.driver = driver;
		this.databaseUrl = databaseUrl;
		this.properties = properties;
	}

	@Override
	public Connection connect() {

		Connection connection = null;
		try {
			if (databaseUrl == null) {
				Logger.printWarning("the database URL is null");

			} else {
				final String databaseDriverAndUrl = driver + ":" + databaseUrl;
				final boolean loadDriverSuccess = loadDriver();
				if (!loadDriverSuccess) {
					throw new Exception();
				}

				final String javaTmpDir = System.getProperty("java.io.tmpdir");
				final String tempFolderPathString = new EnvProviderSystem().getEnv("TEMP");
				System.setProperty("java.io.tmpdir", tempFolderPathString);
				if (properties == null) {
					connection = DriverManager.getConnection(databaseDriverAndUrl);
				} else {
					connection = DriverManager.getConnection(databaseDriverAndUrl, properties);
				}
				if (connection == null) {
					throw new Exception();
				}
				System.setProperty("java.io.tmpdir", javaTmpDir);
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to create a connection to the database:" +
					System.lineSeparator() + databaseUrl);
			Logger.printThrowable(throwable);
		}
		return connection;
	}

	abstract boolean loadDriver();

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
