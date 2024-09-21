package com.utils.writers.file_writers;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.di_boolean.DataItemBoolean;
import com.utils.data_types.data_items.di_byte.DataItemUByte;
import com.utils.data_types.data_items.di_byte.DataItemUByteHex;
import com.utils.data_types.data_items.di_double.DataItemDouble;
import com.utils.data_types.data_items.di_int.DataItemUInt;
import com.utils.data_types.data_items.di_int.DataItemUIntHex;
import com.utils.data_types.data_items.di_long.DataItemULong;
import com.utils.data_types.data_items.di_long.DataItemULongHex;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.writers.file_writers.data.DataTable;
import com.utils.xls.XlsUtils;
import com.utils.xls.cell.XlsCell;
import com.utils.xls.cell.XlsCellBlank;
import com.utils.xls.cell.XlsCellBoolean;
import com.utils.xls.cell.XlsCellNumber;
import com.utils.xls.cell.XlsCellString;
import com.utils.xls.row.XlsRow;
import com.utils.xls.sheet.XlsSheet;
import com.utils.xls.style.FactoryXlsCellStyles;
import com.utils.xls.workbook.XlsxWorkbook;

public final class DataFileWriterXlsx extends AbstractDataFileWriter {

	public static final DataFileWriterXlsx INSTANCE = new DataFileWriterXlsx();

	private DataFileWriterXlsx() {
	}

	@Override
	void writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) throws Exception {

		try (Workbook workbook = XlsxWorkbook.createNew()) {

			final CellStyle cellStyleTitle =
					FactoryXlsCellStyles.createCellStyleTitle(workbook);
			final CellStyle cellStyleRegular =
					FactoryXlsCellStyles.createCellStyleRegular(workbook);
			final CellStyle cellStyleDecimalNumber =
					FactoryXlsCellStyles.createCellStyleDecimalNumber(workbook);

			for (final DataTable dataTable : dataTableList) {
				writeToXlsx(dataTable, workbook,
						cellStyleTitle, cellStyleRegular, cellStyleDecimalNumber);
			}

			XlsUtils.saveWorkbook(workbook, outputPathString);

			Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
			Logger.printLine(outputPathString);
		}
	}

	private static void writeToXlsx(
			final DataTable dataTable,
			final Workbook workbook,
			final CellStyle cellStyleTitle,
			final CellStyle cellStyleRegular,
			final CellStyle cellStyleDecimalNumber) {

		final String displayName = dataTable.getDisplayName();

		final int totalColumnWidth = dataTable.getTotalColumnWidth();

		final TableColumnData[] columnsData = dataTable.getColumnsData();
		final double[] columnWidthRatioArray = createColumnWidthPercentages(columnsData);

		final XlsSheet xlsSheet = new XlsSheet(displayName, totalColumnWidth, columnWidthRatioArray);

		final XlsRow xlsRowTitle = createTitleRow(columnsData, cellStyleTitle);
		final List<XlsRow> xlsRowList = xlsSheet.getXlsRowList();
		xlsRowList.add(xlsRowTitle);

		final List<? extends TableRowData> rowDataList = dataTable.getRowDataList();
		for (final TableRowData tableRowData : rowDataList) {
			fillRowList(tableRowData, cellStyleRegular, cellStyleDecimalNumber, xlsRowList);
		}

		xlsSheet.write(workbook);
	}

	private static double[] createColumnWidthPercentages(
			final TableColumnData[] tableColumnDataArray) {

		double widthWeightSum = 0;
		for (final TableColumnData tableColumn : tableColumnDataArray) {

			final double widthWeight = tableColumn.getWidthWeight();
			widthWeightSum += widthWeight;
		}

		final int tableColumnsLength = tableColumnDataArray.length;
		final double[] columnWidthRatioArray = new double[tableColumnsLength];
		for (int columnIndex = 0; columnIndex < tableColumnsLength; columnIndex++) {

			final TableColumnData tableColumnData = tableColumnDataArray[columnIndex];
			final double widthRatio = tableColumnData.computeWidthRatio(widthWeightSum);
			columnWidthRatioArray[columnIndex] = widthRatio;
		}
		return columnWidthRatioArray;
	}

	private static XlsRow createTitleRow(
			final TableColumnData[] tableColumnDataArray,
			final CellStyle cellStyleTitle) {

		final XlsRow xlsRow = new XlsRow(30f);

		for (final TableColumnData tableColumnData : tableColumnDataArray) {

			final String columnName = tableColumnData.getName();
			xlsRow.getXlsCellList().add(new XlsCellString(cellStyleTitle, columnName));
		}

		return xlsRow;
	}

	private static void fillRowList(
			final TableRowData tableRowData,
			final CellStyle cellStyleRegular,
			final CellStyle cellStyleDecimalNumber,
			final List<XlsRow> xlsRowList) {

		final List<List<XlsCell>> xlsCellsByColumnList = new ArrayList<>();
		final DataItem<?>[] dataItemArray = tableRowData.getDataItemArray();
		for (int i = 0; i < dataItemArray.length; i++) {
			xlsCellsByColumnList.add(new ArrayList<>());
		}

		for (int i = 0; i < dataItemArray.length; i++) {

			final DataItem<?> dataItem = dataItemArray[i];
			final List<XlsCell> xlsCellList = xlsCellsByColumnList.get(i);

			switch (dataItem) {

				case final DataItemBoolean dataItemBoolean -> {

					final boolean booleanValue = dataItemBoolean.createXlsxValue();
					final XlsCell xlsCell = new XlsCellBoolean(cellStyleRegular, booleanValue);
					xlsCellList.add(xlsCell);
				}
				case final DataItemUByteHex dataItemUByteHex -> {

					final byte byteValue = dataItemUByteHex.createXlsxValue();
					if (byteValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(byteValue);
						final XlsCell xlsCell = new XlsCellString(cellStyleRegular, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUByte dataItemUByte -> {

					final byte byteValue = dataItemUByte.createXlsxValue();
					if (byteValue >= 0) {

						final XlsCell xlsCell = new XlsCellNumber(cellStyleRegular, byteValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUIntHex dataItemUIntHex -> {

					final int integerValue = dataItemUIntHex.createXlsxValue();
					if (integerValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(integerValue);
						final XlsCell xlsCell = new XlsCellString(cellStyleRegular, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUInt dataItemUInt -> {

					final int integerValue = dataItemUInt.createXlsxValue();
					if (integerValue >= 0) {

						final XlsCell xlsCell = new XlsCellNumber(cellStyleRegular, integerValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemULongHex dataItemULongHex -> {

					final long longValue = dataItemULongHex.createXlsxValue();
					if (longValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(longValue);
						final XlsCell xlsCell = new XlsCellString(cellStyleRegular, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemULong dataItemULong -> {

					final long longValue = dataItemULong.createXlsxValue();
					if (longValue >= 0) {

						final XlsCell xlsCell = new XlsCellNumber(cellStyleRegular, longValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemDouble dataItemDouble -> {

					final double doubleValue = dataItemDouble.createXlsxValue();
					if (!Double.isNaN(doubleValue)) {

						final XlsCell xlsCell = new XlsCellNumber(cellStyleDecimalNumber, doubleValue);
						xlsCellList.add(xlsCell);
					}
				}
				case null -> {
				}
				default -> {
					final Object value = dataItem.createXlsxValue();
					if (value != null) {

						final String stringValue = value.toString();
						for (int j = 0; j < stringValue.length(); j += TableRowData.XLSX_CELL_CHARACTER_LIMIT) {

							final String stringValuePart = stringValue.substring(j,
									Math.min(j + TableRowData.XLSX_CELL_CHARACTER_LIMIT, stringValue.length()));
							final XlsCell xlsCell = new XlsCellString(cellStyleRegular, stringValuePart);
							xlsCellList.add(xlsCell);
						}
					}
				}
			}
		}

		int maxRowCount = 1;
		for (final List<XlsCell> xlsCellList : xlsCellsByColumnList) {
			maxRowCount = Math.max(maxRowCount, xlsCellList.size());
		}

		for (int i = 0; i < maxRowCount; i++) {

			final XlsRow xlsRow = new XlsRow();
			for (final List<XlsCell> xlsCellList : xlsCellsByColumnList) {

				final XlsCell xlsCell;
				if (i < xlsCellList.size()) {
					xlsCell = xlsCellList.get(i);
				} else {
					xlsCell = new XlsCellBlank(cellStyleRegular);
				}
				xlsRow.getXlsCellList().add(xlsCell);
			}
			xlsRowList.add(xlsRow);
		}
	}

	@Override
	public String getExtension() {
		return "xlsx";
	}

	@Override
	public int getOrder() {
		return 201;
	}
}
