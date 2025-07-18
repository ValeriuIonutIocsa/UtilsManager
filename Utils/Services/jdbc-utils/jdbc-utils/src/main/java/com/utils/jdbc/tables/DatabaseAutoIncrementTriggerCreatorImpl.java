package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.Statement;

import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseAutoIncrementTriggerCreatorImpl implements DatabaseAutoIncrementTriggerCreator {

	@Override
	public boolean work(
			final Connection connection,
			final String triggerName,
			final String sequenceName,
			final String tableName,
			final String columnName) {

		boolean success = false;
		try {
			Logger.printProgress("creating trigger \"" + triggerName + "\"");

			final String sql = createSql(triggerName, sequenceName, tableName, columnName);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (Statement statement = connection.createStatement()) {
				statement.execute(sql);
			}

			Logger.printStatus("Successfully created trigger \"" + triggerName + "\".");
			success = true;

		} catch (final Throwable throwable) {
			Logger.printError("failed to create trigger \"" + triggerName + "\"");
			Logger.printThrowable(throwable);
		}
		return success;
	}

	private static String createSql(
			final String triggerName,
			final String sequenceName,
			final String tableName,
			final String columnName) {

		return "CREATE OR REPLACE TRIGGER " + triggerName +
				" BEFORE INSERT ON " + tableName +
				" FOR EACH ROW BEGIN SELECT " +
				sequenceName + ".NEXTVAL INTO " +
				":new." + columnName + " FROM dual; END;";
	}
}
