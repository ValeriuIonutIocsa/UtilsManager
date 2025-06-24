package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.Statement;

import com.utils.io.ResourceFileUtils;
import com.utils.jdbc.utils.JdbcUtils;
import com.utils.log.Logger;

public class DatabaseDropEverythingImpl implements DatabaseDropEverything {

	@Override
	public boolean work(
			final Connection connection) {

		try {
			Logger.printProgress("dropping everything from the database");

			final String sql = ResourceFileUtils.resourceFileToString("com/utils/jdbc/tables/drop_eveything.sql");
			if (JdbcUtils.isDebugMode()) {

				Logger.printProgress("executing SQL code:");
				Logger.printLine(sql);
			}
			try (Statement statement = connection.createStatement()) {
				statement.execute(sql);
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to drop everything from the database");
			Logger.printThrowable(throwable);
		}
		return true;
	}
}
