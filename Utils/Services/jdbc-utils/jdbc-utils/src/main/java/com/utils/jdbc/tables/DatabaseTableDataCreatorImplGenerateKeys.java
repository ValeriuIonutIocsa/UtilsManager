package com.utils.jdbc.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Predicate;

import com.utils.data_types.db.TableRowDataWithId;

public class DatabaseTableDataCreatorImplGenerateKeys<
		TableRowDataT extends TableRowDataWithId>
		extends DatabaseTableDataCreatorImpl<TableRowDataT> {

	private final int generatedKeysColumnIndex;

	public DatabaseTableDataCreatorImplGenerateKeys(
			final int generatedKeysColumnIndex) {

		this.generatedKeysColumnIndex = generatedKeysColumnIndex;
	}

	@Override
	PreparedStatement createPreparedStatement(
			final Connection connection,
			final String sql) throws SQLException {

		return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	}

	@Override
	protected void executeBatch(
			final PreparedStatement preparedStatement,
			final List<TableRowDataT> tableRowDataList,
			final int startIndex,
			final int batchRowCount) throws SQLException {

		super.executeBatch(preparedStatement, tableRowDataList, startIndex, batchRowCount);

		int lastGeneratedKey = -1;
		final ResultSet resultSetGeneratedKeys = preparedStatement.getGeneratedKeys();
		while (resultSetGeneratedKeys.next()) {
			lastGeneratedKey = resultSetGeneratedKeys.getInt(1);
		}

		if (lastGeneratedKey > 0) {

			for (int i = 0; i < batchRowCount; i++) {

				final int index = startIndex + i;
				final int generatedKey = lastGeneratedKey - batchRowCount + i + 1;
				final TableRowDataT tableRowData = tableRowDataList.get(index);
				tableRowData.setId(generatedKey);
			}
		}
	}

	@Override
	Predicate<Integer> createExcludedColumnIndexPredicate() {

		return columnIndex -> columnIndex == generatedKeysColumnIndex;
	}
}
