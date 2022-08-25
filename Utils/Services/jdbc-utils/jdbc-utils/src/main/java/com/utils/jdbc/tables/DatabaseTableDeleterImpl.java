package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.log.Logger;

public class DatabaseTableDeleterImpl implements DatabaseTableDeleter {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo,
			final String whereClause) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.getName();
			Logger.printProgress("deleting data from table \"" + tableName + "\"");

			final String sql = createSql(databaseTableInfo, whereClause);
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				setParameters(preparedStatement);
				preparedStatement.execute();
			}

			Logger.printStatus("Successfully deleted data from table \"" + tableName + "\".");
			success = true;

		} catch (final Exception exc) {
			final String tableName = databaseTableInfo.getName();
			Logger.printError("failed to delete data from table \"" + tableName + "\"");
			Logger.printException(exc);
		}
		return success;
	}

	protected void setParameters(
			final PreparedStatement preparedStatement) throws Exception {
	}

	static String createSql(
			final DatabaseTableInfo databaseTableInfo,
			final String whereClause) {

		final String tableName = databaseTableInfo.getName();
		final StringBuilder sbSql = new StringBuilder("DELETE FROM \"" + tableName + "\"");
		if (whereClause != null) {
			sbSql.append(' ').append(whereClause);
		}
		return sbSql.toString();
	}
}
