package com.utils.jdbc;

import java.sql.Connection;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.jdbc.data_sources.DataSource;
import com.utils.jdbc.data_sources.DataSourcePostgresql;
import com.utils.jdbc.data_sources.supabase.SupabaseDbUtils;

public class DataConnectionSupabaseTest {

	@Test
	void testConnection() {

		final Properties properties = SupabaseDbUtils.createProperties();
		final String databaseUrl = properties.getProperty("databaseUrl");
		final DataSource dataSource = new DataSourcePostgresql(databaseUrl, properties);

		final boolean success = new DatabaseConnection(dataSource) {

			@Override
			protected boolean statements(
					final Connection connection) {

				return true;
			}
		}.executeStatements();
		Assertions.assertTrue(success);
	}
}
