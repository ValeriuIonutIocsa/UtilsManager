package com.utils.jdbc.tables;

import java.sql.Connection;

public interface DatabaseDropEverything {

	boolean work(
			Connection connection);
}
