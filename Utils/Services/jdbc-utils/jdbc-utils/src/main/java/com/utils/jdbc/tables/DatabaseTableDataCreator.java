package com.utils.jdbc.tables;

import java.sql.Connection;
import java.util.List;

import com.utils.data_types.db.DatabaseTableInfo;
import com.utils.data_types.table.TableRowData;

public interface DatabaseTableDataCreator<
		TableRowDataT extends TableRowData> {

	boolean work(
			Connection connection,
			DatabaseTableInfo databaseTableInfo,
			List<TableRowDataT> tableRowDataList,
			boolean verbose);
}
