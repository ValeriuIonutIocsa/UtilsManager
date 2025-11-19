package com.utils.jdbc;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;
import com.utils.jdbc.data_sources.DataSourcePostgresql;
import com.utils.jdbc.data_sources.FactoryDataSourcePostgresql;

class DatabaseConnectionPostgresqlTest {

	@Test
	void testConnection() {

		final String propertiesFilePathString = PathUtils.computePath(
				PathUtils.createRootPath(), "IVI_PERS", "Tmp",
				"WeatherDatabase", "Inputs", "postgresql_db.properties");
		final DataSourcePostgresql dataSourcePostgresql =
				FactoryDataSourcePostgresql.newInstance(propertiesFilePathString);

		final boolean success = new AbstractDatabaseConnectionPostgresql(dataSourcePostgresql) {

			@Override
			protected boolean statements(
					final Connection connection) {

				return true;
			}
		}.executeStatements();
		Assertions.assertTrue(success);
	}
}
