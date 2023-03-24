package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseTableDropperImpl implements DatabaseTableDropper {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.getName();
			Logger.printProgress("dropping table \"" + tableName + "\"");

			final String sql = createSql(databaseTableInfo);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.execute();
			}

			Logger.printStatus("Successfully dropped table \"" + tableName + "\".");
			success = true;

		} catch (final Exception exc) {
			final String tableName = databaseTableInfo.getName();
			Logger.printError("failed to drop table \"" + tableName + "\"");
			Logger.printException(exc);
		}
		return success;
	}

	static String createSql(
			final DatabaseTableInfo databaseTableInfo) {

		final String tableName = databaseTableInfo.getName();
		return "DROP TABLE IF EXISTS \"" + tableName + "\"";
	}
}
