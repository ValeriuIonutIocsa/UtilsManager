package com.utils.jdbc.tables;

import java.sql.Connection;

public interface DatabaseSequenceCreator {

	boolean work(
			Connection connection,
			String sequenceName,
			int cacheSize);
}
