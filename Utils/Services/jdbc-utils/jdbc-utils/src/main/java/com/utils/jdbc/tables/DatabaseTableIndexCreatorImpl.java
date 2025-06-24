package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.db.DatabaseTableColumn;
import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseTableIndexCreatorImpl implements DatabaseTableIndexCreator {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo,
			final String indexName,
			final int[] columnIndices) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.name();
			Logger.printProgress("creating table \"" + tableName + "\"");

			final String sql = createSql(databaseTableInfo, indexName, columnIndices);
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
			final DatabaseTableInfo databaseTableInfo,
			final String indexName,
			final int[] columnIndices) {

		final String tableName = databaseTableInfo.name();

		final List<Integer> columnIndexList = new ArrayList<>();
		for (final int columnIndex : columnIndices) {
			columnIndexList.add(columnIndex);
		}

		final List<String> columnList = new ArrayList<>();
		fillColumnList(databaseTableInfo, columnIndexList, columnList);

		final String columnsString = StringUtils.join(columnList, ',');
		return "CREATE INDEX \"" + indexName + "\" ON \"" + tableName + "\"(" + columnsString + ")";
	}

	private static void fillColumnList(
			final DatabaseTableInfo databaseTableInfo,
			final List<Integer> columnIndexList,
			final List<String> columnList) {

		final DatabaseTableColumn[] databaseTableColumns = databaseTableInfo.databaseTableColumnArray();
		for (int columnIndex = 0; columnIndex < databaseTableColumns.length; columnIndex++) {

			if (columnIndexList.contains(columnIndex)) {

				final DatabaseTableColumn databaseTableColumn = databaseTableColumns[columnIndex];
				final String columnName = databaseTableColumn.name();
				final String columnString = "\"" + columnName + "\"";
				columnList.add(columnString);
			}
		}
	}
}
