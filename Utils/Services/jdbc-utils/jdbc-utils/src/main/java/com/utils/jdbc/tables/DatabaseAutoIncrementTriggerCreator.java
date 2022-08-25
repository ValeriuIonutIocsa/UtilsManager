package com.utils.jdbc.tables;

import java.sql.Connection;

public interface DatabaseAutoIncrementTriggerCreator {

	boolean work(
			Connection connection,
			String triggerName,
			String sequenceName,
			String tableName,
			String columnName);
}
