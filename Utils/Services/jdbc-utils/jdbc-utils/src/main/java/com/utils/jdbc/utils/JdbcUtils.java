package com.utils.jdbc.utils;

import java.io.PrintStream;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.utils.annotations.ApiMethod;
import com.utils.csv.AbstractCsvWriter;
import com.utils.csv.CsvWriter;
import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.table.TableRowData;
import com.utils.log.Logger;

public final class JdbcUtils {

	private JdbcUtils() {
	}

	@ApiMethod
	public static int serializeToDatabase(
			final TableRowData element,
			final PreparedStatement preparedStatement) throws SQLException {

		final DataItem<?>[] dataItemArray = element.getDataItemArray();
		for (int i = 0; i < dataItemArray.length; i++) {

			final DataItem<?> dataItem = dataItemArray[i];
			if (dataItem != null) {
				dataItem.serializeToDataBase(i + 1, preparedStatement);
			}
		}
		return dataItemArray.length;
	}

	@ApiMethod
	public static void printResultSet(
			final ResultSet resultSet) {

		final String resultSetToString = resultSetToString(resultSet);
		Logger.printLine(resultSetToString);
	}

	@ApiMethod
	public static String resultSetToString(
			final ResultSet resultSet) {

		final StringBuilder stringBuilder = new StringBuilder();

		try {
			final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			final int columnsNumber = resultSetMetaData.getColumnCount();
			while (resultSet.next()) {

				for (int i = 1; i <= columnsNumber; i++) {

					if (i > 1) {
						stringBuilder.append(",  ");
					}

					final String columnName = resultSetMetaData.getColumnName(i);
					final String columnValue = resultSet.getString(i);
					stringBuilder.append(columnName).append(" { ").append(columnValue).append(" }");
				}
				stringBuilder.append(System.lineSeparator());
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}

		return stringBuilder.toString();
	}

	@ApiMethod
	public static void printResultSetToCsv(
			final ResultSet resultSet,
			final Path outputPath) {

		final CsvWriter csvWriter = new AbstractCsvWriter("JDBC result set", outputPath) {

			@Override
			protected void write(
					final PrintStream printStream) {

				try {
					final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

					final int columnCount = resultSetMetaData.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						final String columnName = resultSetMetaData.getColumnName(i);
						printStream.print(columnName);
						if (i < columnCount) {
							printStream.print(',');
						}
					}
					printStream.println();

					while (resultSet.next()) {

						for (int i = 1; i <= columnCount; i++) {

							final String columnValue = resultSet.getString(i);
							printStream.print(columnValue);
							if (i < columnCount) {
								printStream.print(',');
							}
						}
						printStream.println();
					}

				} catch (final Exception exc) {
					Logger.printException(exc);
				}
			}
		};
		csvWriter.writeCsv();
	}
}
