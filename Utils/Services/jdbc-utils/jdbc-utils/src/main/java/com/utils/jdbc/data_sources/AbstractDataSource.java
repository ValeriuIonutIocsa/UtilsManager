package com.utils.jdbc.data_sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.string.exc.SilentException;

abstract class AbstractDataSource implements DataSource {

	private final String driver;
	private final String databaseUrl;
	private final String tempFolderPathString;
	private final Properties properties;

	AbstractDataSource(
			final String driver,
			final String databaseUrl,
			final String tempFolderPathString,
			final Properties properties) {

		this.driver = driver;
		this.databaseUrl = databaseUrl;
		this.tempFolderPathString = tempFolderPathString;
		this.properties = properties;
	}

	@Override
	public Connection connect() {

		Connection connection = null;
		final String javaTmpDir = System.getProperty("java.io.tmpdir");
		System.setProperty("java.io.tmpdir", tempFolderPathString);
		try {
			if (databaseUrl == null) {
				Logger.printWarning("the database URL is null");

			} else {
				final String databaseDriverAndUrl = driver + ":" + databaseUrl;
				final boolean loadDriverSuccess = loadDriver();
				if (!loadDriverSuccess) {
					throw new SilentException();
				}

				if (properties == null) {
					connection = DriverManager.getConnection(databaseDriverAndUrl);
				} else {
					connection = DriverManager.getConnection(databaseDriverAndUrl, properties);
				}
				if (connection == null) {
					throw new SilentException();
				}
			}

		} catch (final SilentException ignored) {
			Logger.printError("failed to create a connection to the database:" +
					System.lineSeparator() + databaseUrl);

		} catch (final Throwable throwable) {
			Logger.printError("failed to create a connection to the database:" +
					System.lineSeparator() + databaseUrl);
			Logger.printThrowable(throwable);

		} finally {
			System.setProperty("java.io.tmpdir", javaTmpDir);
		}
		return connection;
	}

	abstract boolean loadDriver();

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
