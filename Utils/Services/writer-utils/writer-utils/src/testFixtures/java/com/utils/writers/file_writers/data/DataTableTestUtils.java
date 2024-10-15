package com.utils.writers.file_writers.data;

import java.util.List;

public final class DataTableTestUtils {

	private DataTableTestUtils() {
	}

	public static DataTable createTestDataTable() {

		final List<TestTableRowData> testTableRowDataList = List.of(
				new TestTableRowData("Name1", 1, 2),
				new TestTableRowData("Name2", 3, 4),
				new TestTableRowData("Name3", 5, 6));
		return new DataTable(TestTableRowData.DATA_INFO, testTableRowDataList);
	}
}
