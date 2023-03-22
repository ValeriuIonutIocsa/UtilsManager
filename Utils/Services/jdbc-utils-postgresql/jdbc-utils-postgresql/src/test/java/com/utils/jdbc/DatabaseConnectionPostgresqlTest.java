package com.utils.jdbc;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DatabaseConnectionPostgresqlTest {

	@Test
	void testConnection() {

		final boolean success = new DatabaseConnectionPostgresql() {

			@Override
			protected boolean statements(
					final Connection connection) {

				return true;
			}
		}.executeStatements();
		Assertions.assertTrue(success);
	}
}
