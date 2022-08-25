package com.utils.jdbc.tables;

import java.sql.Connection;

import com.utils.data_types.db.DatabaseTableInfo;

public interface DatabaseTableDeleter {

	boolean work(
			Connection connection,
			DatabaseTableInfo databaseTableInfo,
			String whereClause);
}
