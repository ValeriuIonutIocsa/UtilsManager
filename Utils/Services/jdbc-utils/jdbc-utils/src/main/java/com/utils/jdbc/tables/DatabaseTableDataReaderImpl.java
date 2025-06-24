package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.db.DatabaseTableColumn;
import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;

public class DatabaseTableDataReaderImpl<
		ObjectT> implements DatabaseTableDataReader<ObjectT> {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo,
			final int[] columnIndices,
			final String whereClause,
			final Function<ResultSet, ObjectT> deserializer,
			final Collection<ObjectT> dataList,
			final boolean verbose) {

		boolean success = false;
		try {
			if (verbose) {
				final String tableName = databaseTableInfo.name();
				Logger.printProgress("retrieving data from table \"" + tableName + "\"");
			}

			ProgressIndicators.getInstance().update(0);

			final String sql = createSql(databaseTableInfo, columnIndices, whereClause);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				setParameters(preparedStatement);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {

					int rowIndex = 0;
					int rowCount = Integer.MAX_VALUE;
					while (resultSet.next()) {

						if (rowIndex == 0) {

							final ResultSetMetaData metaData = resultSet.getMetaData();
							final int columnCount = metaData.getColumnCount();
							rowCount = resultSet.getInt(columnCount);
						}

						rowIndex++;
						if (rowIndex % 1000 == 0) {
							ProgressIndicators.getInstance().update(rowIndex, rowCount);
						}

						final ObjectT data = deserializer.apply(resultSet);
						if (data != null) {
							if (dataList != null) {
								dataList.add(data);
							}
						}
					}
				}
			}

			if (verbose) {
				final String tableName = databaseTableInfo.name();
				Logger.printStatus("Successfully retrieved data from table \"" + tableName + "\".");
			}
			success = true;

		} catch (final Throwable throwable) {
			if (verbose) {
				final String tableName = databaseTableInfo.name();
				Logger.printError("failed to retrieve the data from table \"" + tableName + "\"");
			}
			Logger.printThrowable(throwable);

		} finally {
			ProgressIndicators.getInstance().update(0);
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
			final int[] columnIndices,
			final String whereClause) {

		final String tableName = databaseTableInfo.name();

		final String columnsString;
		if (ArrayUtils.getLength(columnIndices) > 0) {

			final List<String> columnList = new ArrayList<>();
			fillColumnList(databaseTableInfo, columnIndices, columnList);

			columnsString = StringUtils.join(columnList, ',');

		} else {
			columnsString = "*";
		}

		final StringBuilder sbSql = new StringBuilder(
				"SELECT " + columnsString + ",COUNT(*) OVER() FROM \"" + tableName + "\"");
		if (whereClause != null) {
			sbSql.append(' ').append(whereClause);
		}
		return sbSql.toString();
	}

	private static void fillColumnList(
			final DatabaseTableInfo databaseTableInfo,
			final int[] columnIndices,
			final List<String> columnList) {

		final List<Integer> columnIndexList = new ArrayList<>();
		for (final int columnIndex : columnIndices) {
			columnIndexList.add(columnIndex);
		}

		final DatabaseTableColumn[] databaseTableColumns = databaseTableInfo.databaseTableColumnArray();
		for (int columnIndex = 0; columnIndex < databaseTableColumns.length; columnIndex++) {

			if (columnIndexList.contains(columnIndex)) {

				final DatabaseTableColumn databaseTableColumn = databaseTableColumns[columnIndex];
				final String columnName = databaseTableColumn.name();
				columnList.add("\"" + columnName + "\"");
			}
		}
	}
}
