package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

import com.utils.data_types.db.DatabaseTableColumn;
import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.data_types.table.TableRowData;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;

public class DatabaseTableDataCreatorImpl<
		TableRowDataT extends TableRowData>
		implements DatabaseTableDataCreator<TableRowDataT> {

	@Override
	public boolean work(
			final Connection connection,
			final DatabaseTableInfo databaseTableInfo,
			final List<TableRowDataT> tableRowDataList,
			final boolean verbose) {

		boolean success = false;
		try {
			final String tableName = databaseTableInfo.getName();
			if (verbose) {
				Logger.printProgress("exporting data to table \"" + tableName + "\"");
			}

			ProgressIndicators.getInstance().update(0);

			final boolean oldAutoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			final Predicate<Integer> excludedColumnIndexPredicate = createExcludedColumnIndexPredicate();

			final String sql = createSql(databaseTableInfo, excludedColumnIndexPredicate);
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (PreparedStatement preparedStatement = createPreparedStatement(connection, sql)) {

				exportData(tableRowDataList, excludedColumnIndexPredicate, preparedStatement);
				connection.commit();
				if (verbose) {
					Logger.printStatus("Successfully exported data to table \"" + tableName + "\".");
				}
				success = true;

			} catch (final Exception exc) {
				Logger.printException(exc);
				connection.rollback();

			} finally {
				connection.setAutoCommit(oldAutoCommit);
			}

		} catch (final Exception exc) {
			Logger.printException(exc);

		} finally {
			ProgressIndicators.getInstance().update(0);
		}

		if (!success) {
			final String tableName = databaseTableInfo.getName();
			if (verbose) {
				Logger.printError("failed to export the data to table \"" + tableName + "\"");
			}
		}
		return success;
	}

	PreparedStatement createPreparedStatement(
			final Connection connection,
			final String sql) throws SQLException {

		return connection.prepareStatement(sql);
	}

	protected void exportData(
			final List<TableRowDataT> tableRowDataList,
			final Predicate<Integer> excludedColumnIndexPredicate,
			final PreparedStatement preparedStatement) throws SQLException {

		int index = 0;
		int startIndex = 0;
		int batchRowCount = 0;
		final int count = tableRowDataList.size();
		for (final TableRowDataT tableRowData : tableRowDataList) {

			index++;
			if (index % 1000 == 0) {

				ProgressIndicators.getInstance().update(index, count);
				executeBatch(preparedStatement, tableRowDataList, startIndex, batchRowCount);
				startIndex = index - 1;
				batchRowCount = 0;
			}

			JdbcUtils.serializeToDatabase(tableRowData, excludedColumnIndexPredicate, preparedStatement);
			preparedStatement.addBatch();
			batchRowCount++;
		}
		executeBatch(preparedStatement, tableRowDataList, startIndex, batchRowCount);
	}

	/**
	 * @param preparedStatement
	 *            preparedStatement
	 * @param tableRowDataList
	 *            tableRowDataList
	 * @param startIndex
	 *            startIndex
	 * @param batchRowCount
	 *            batchRowCount
	 * @throws SQLException
	 *             SQLException
	 */
	protected void executeBatch(
			final PreparedStatement preparedStatement,
			final List<TableRowDataT> tableRowDataList,
			final int startIndex,
			final int batchRowCount) throws SQLException {

		preparedStatement.executeBatch();
	}

	static String createSql(
			final DatabaseTableInfo databaseTableInfo,
			final Predicate<Integer> excludedColumnIndexPredicate) {

		final StringBuilder sbSql = new StringBuilder(200);

		final String tableName = databaseTableInfo.getName();
		sbSql.append("INSERT INTO \"").append(tableName).append("\" (");

		final DatabaseTableColumn[] columns = databaseTableInfo.getDatabaseTableColumnArray();
		final int columnCount = columns.length;
		for (int i = 0; i < columnCount; i++) {

			final boolean excludedColumnIndex = excludedColumnIndexPredicate.test(i);
			if (!excludedColumnIndex) {

				final DatabaseTableColumn column = columns[i];
				final String columnName = column.getName();
				sbSql.append('"').append(columnName).append('"');
				if (i < columnCount - 1) {
					sbSql.append(", ");
				}
			}
		}

		sbSql.append(") VALUES (");

		for (int i = 0; i < columnCount; i++) {

			final boolean excludedColumnIndex = excludedColumnIndexPredicate.test(i);
			if (!excludedColumnIndex) {

				sbSql.append('?');
				if (i < columnCount - 1) {
					sbSql.append(", ");
				}
			}
		}

		sbSql.append(')');
		return sbSql.toString();
	}

	Predicate<Integer> createExcludedColumnIndexPredicate() {

		return columnIndex -> false;
	}
}
