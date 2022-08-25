package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.Statement;

import com.utils.io.IoUtils;
import com.utils.log.Logger;

public class DatabaseDropEverythingImpl implements DatabaseDropEverything {

	@Override
	public boolean work(
			final Connection connection) {

		try {
			Logger.printProgress("dropping everything from the database");

			final String sql = IoUtils.resourceFileToString("com/utils/jdbc/tables/drop_eveything.sql");
			try (Statement statement = connection.createStatement()) {
				statement.execute(sql);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to drop everything from the database");
			Logger.printException(exc);
		}
		return true;
	}
}
