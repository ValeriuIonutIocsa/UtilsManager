package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.utils.data_types.db.DatabaseTableColumn;
import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseTableCreatorImpl implements DatabaseTableCreator {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.name();
			Logger.printProgress("creating table \"" + tableName + "\"");

			final String sql = createSql(databaseTableInfo);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.execute();
			}

			Logger.printStatus("Successfully created table \"" + tableName + "\".");
			success = true;

		} catch (final Throwable throwable) {
			final String tableName = databaseTableInfo.name();
			Logger.printError("failed to create table \"" + tableName + "\"");
			Logger.printThrowable(throwable);
		}
		return success;
	}

	static String createSql(
			final DatabaseTableInfo databaseTableInfo) {

		final String tableName = databaseTableInfo.name();
		final StringBuilder sbSql = new StringBuilder("CREATE TABLE \"" + tableName + "\" (");

		final List<String> argumentList = new ArrayList<>();

		final DatabaseTableColumn[] columns = databaseTableInfo.databaseTableColumnArray();
		for (final DatabaseTableColumn column : columns) {

			final String name = column.name();
			final String type = column.type();
			final String argument = "\"" + name + "\" " + type;
			argumentList.add(argument);
		}

		final String[] constraints = databaseTableInfo.constraintArray();
		if (constraints != null) {
			Collections.addAll(argumentList, constraints);
		}

		final int argumentsCount = argumentList.size();
		for (int i = 0; i < argumentsCount; i++) {

			final String argument = argumentList.get(i);
			sbSql.append(argument);
			if (i + 1 < argumentsCount) {
				sbSql.append(", ");
			}
		}

		sbSql.append(')');
		return sbSql.toString();
	}
}
