package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseTableDeleterImpl implements DatabaseTableDeleter {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo,
			final String whereClause) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.name();
			Logger.printProgress("deleting data from table \"" + tableName + "\"");

			final String sql = createSql(databaseTableInfo, whereClause);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				setParameters(preparedStatement);
				preparedStatement.execute();
			}

			Logger.printStatus("Successfully deleted data from table \"" + tableName + "\".");
			success = true;

		} catch (final Throwable throwable) {
			final String tableName = databaseTableInfo.name();
			Logger.printError("failed to delete data from table \"" + tableName + "\"");
			Logger.printThrowable(throwable);
		}
		return success;
	}

	/**
	 * @param preparedStatement
	 *            preparedStatement
	 * @throws Exception
	 *             Exception
	 */
	protected void setParameters(
			final PreparedStatement preparedStatement) throws Exception {
	}

	static String createSql(
			final DatabaseTableInfo databaseTableInfo,
			final String whereClause) {

		final String tableName = databaseTableInfo.name();
		final StringBuilder sbSql = new StringBuilder("DELETE FROM \"" + tableName + "\"");
		if (whereClause != null) {
			sbSql.append(' ').append(whereClause);
		}
		return sbSql.toString();
	}
}
