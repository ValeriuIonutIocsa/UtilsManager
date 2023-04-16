package com.utils.jdbc;

import java.sql.Connection;

import com.utils.jdbc.data_sources.DataSource;
import com.utils.log.Logger;

public abstract class AbstractDatabaseConnection {

	private final DataSource dataSource;

	public AbstractDatabaseConnection(
			final DataSource dataSource) {

		this.dataSource = dataSource;
	}

	public boolean executeStatements() {

		boolean success = false;
		try (Connection connection = dataSource.connect()) {

			if (connection != null) {
				success = statements(connection);
			}

		} catch (final Exception exc) {
			Logger.printError("error occurred while executing SQL statements");
			Logger.printException(exc);
		}
		return success;
	}

	protected abstract boolean statements(
			Connection connection);

	protected void failedToConnect() {
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}
