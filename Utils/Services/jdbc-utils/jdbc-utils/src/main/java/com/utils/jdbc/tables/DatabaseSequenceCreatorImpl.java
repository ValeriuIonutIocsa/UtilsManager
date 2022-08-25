package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.utils.log.Logger;

public class DatabaseSequenceCreatorImpl implements DatabaseSequenceCreator {

	@Override
	public boolean work(
			final Connection connection,
			final String sequenceName,
			final int cacheSize) {

		boolean success = false;
		try {
			Logger.printProgress("creating sequence \"" + sequenceName + "\"");

			final String sql = createSql(sequenceName, cacheSize);
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.execute();
			}

			Logger.printStatus("Successfully created sequence \"" + sequenceName + "\".");
			success = true;

		} catch (final Exception exc) {
			Logger.printError("failed to create sequence \"" + sequenceName + "\"");
			Logger.printException(exc);
		}
		return success;
	}

	private static String createSql(
			final String sequenceName,
			final int cacheSize) {

		return "CREATE SEQUENCE \"" + sequenceName + "\" " +
				"MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE " + cacheSize;
	}
}
