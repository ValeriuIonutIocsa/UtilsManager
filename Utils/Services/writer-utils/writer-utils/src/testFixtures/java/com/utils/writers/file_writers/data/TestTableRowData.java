package com.utils.writers.file_writers.data;

import com.utils.data_types.DataInfo;
import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.di_int.FactoryDataItemUInt;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;

public record TestTableRowData(
		String name,
		int size,
		int count) implements TableRowData {

	private static final TableColumnData[] COLUMNS = {

			new TableColumnData("Name", "Name", 20),
			new TableColumnData("Size", "Size", 10),
			new TableColumnData("Count", "Count", 10)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {

		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(name),
				FactoryDataItemUInt.newInstance(size),
				FactoryDataItemUInt.newInstance(count)
		};
	}

	public static final DataInfo DATA_INFO = new DataInfo("-test_table", "TestTable",
			"TestTableRowDataElements", "TestTableRowDataElement",
			-1, COLUMNS, COLUMNS);
}
