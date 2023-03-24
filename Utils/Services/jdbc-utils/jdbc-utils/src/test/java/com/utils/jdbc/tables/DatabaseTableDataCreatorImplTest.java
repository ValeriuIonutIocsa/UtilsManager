package com.utils.jdbc.tables;

import org.junit.jupiter.api.Test;

import com.utils.data_types.db.DatabaseTableColumn;
import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.log.Logger;

class DatabaseTableDataCreatorImplTest {

	@Test
	void testCreateSql() {

		final DatabaseTableInfo databaseTableInfo = createDatabaseTableInfo();

		final String sql = DatabaseTableDataCreatorImpl.createSql(databaseTableInfo, columnIndex -> false);
		Logger.printLine(sql);
	}

	private static DatabaseTableInfo createDatabaseTableInfo() {

		final DatabaseTableColumn[] columns = {
				new DatabaseTableColumn("UserName", "VARCHAR2(100) NOT NULL"),
				new DatabaseTableColumn("DisplayName", "VARCHAR2(300)"),
				new DatabaseTableColumn("GivenName", "VARCHAR2(300)"),
				new DatabaseTableColumn("Surname", "VARCHAR2(100)"),
				new DatabaseTableColumn("EmailAddress", "VARCHAR2(100)"),
				new DatabaseTableColumn("PhoneNumber", "VARCHAR2(100)"),
				new DatabaseTableColumn("Company", "VARCHAR2(100)"),
				new DatabaseTableColumn("Department", "VARCHAR2(100)"),
				new DatabaseTableColumn("Country", "VARCHAR2(100)"),
				new DatabaseTableColumn("City", "VARCHAR2(100)"),
				new DatabaseTableColumn("Street", "VARCHAR2(100)"),
				new DatabaseTableColumn("Office", "VARCHAR2(100)")
		};

		final String[] constraints = {
				"CONSTRAINT \"PK_AGGREGATE_NAME\" PRIMARY KEY (\"" + columns[0].getName() + "\")"
		};

		return new DatabaseTableInfo("DomainUsers", columns, constraints);
	}
}
