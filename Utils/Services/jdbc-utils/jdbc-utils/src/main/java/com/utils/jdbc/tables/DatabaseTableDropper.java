package com.utils.jdbc.tables;

import java.sql.Connection;

import com.utils.data_types.db.DatabaseTableInfo;

public interface DatabaseTableDropper {

	boolean work(
			Connection connection,
			DatabaseTableInfo databaseTableInfo);
}
