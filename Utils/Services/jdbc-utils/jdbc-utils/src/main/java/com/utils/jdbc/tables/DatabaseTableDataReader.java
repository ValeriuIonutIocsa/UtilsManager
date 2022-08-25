package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.function.Function;

import com.utils.data_types.db.DatabaseTableInfo;

public interface DatabaseTableDataReader<
		ObjectT> {

	boolean work(
			Connection connection,
			DatabaseTableInfo databaseTableInfo,
			int[] columnIndices,
			String whereClause,
			Function<ResultSet, ObjectT> deserializer,
			Collection<ObjectT> dataList,
			boolean verbose);
}
