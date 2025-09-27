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

public final class DataFileWriterXlsx extends AbstractDataFileWriter {

	public static final DataFileWriterXlsx INSTANCE = new DataFileWriterXlsx();

	private static final int DATA_ROW_INDEX_LIMIT = 1_048_576;

	private DataFileWriterXlsx() {
	}

	@Override
	String writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) throws Exception {

		try (Workbook workbook = XlsUtils.createNewWorkbook()) {

			final WriterCellStyles writerCellStyles = new WriterCellStyles();
			writerCellStyles.initialize(workbook);

			for (final DataTable dataTable : dataTableList) {
				writeToXlsx(dataTable, workbook, writerCellStyles);
			}

			XlsUtils.saveWorkbook(workbook, outputPathString);

			Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
			Logger.printLine(outputPathString);
		}
		return outputPathString;
	}

	private static void writeToXlsx(
			final DataTable dataTable,
			final Workbook workbook,
			final WriterCellStyles writerCellStyles) {

		final String displayName = dataTable.getDisplayName();

		final int totalColumnWidth = dataTable.getTotalColumnWidth();

		final TableColumnData[] columnsData = dataTable.getColumnsData();
		final double[] columnWidthRatioArray = createColumnWidthPercentages(columnsData);

		final XlsSheet xlsSheet = new XlsSheet(displayName, totalColumnWidth, columnWidthRatioArray);

		final CellStyle cellStyleTitle = writerCellStyles.getCellStyleTitle();
		final XlsRow xlsRowTitle = createTitleRow(columnsData, cellStyleTitle);
		final List<XlsRow> xlsRowList = xlsSheet.getXlsRowList();
		xlsRowList.add(xlsRowTitle);

		final List<? extends TableRowData> rowDataList = dataTable.getRowDataList();
		int rowIndex = 2;
		for (final TableRowData rowData : rowDataList) {

			if (rowIndex == DATA_ROW_INDEX_LIMIT) {

				Logger.printWarning("the number of rows exceeds XLSX limit of " +
						StrUtils.positiveIntToString(DATA_ROW_INDEX_LIMIT, true) +
						"; the remaining rows will not be written to the file.");
				break;
			}
			fillRowList(rowData, workbook, writerCellStyles, xlsRowList);
			rowIndex++;
		}

		xlsSheet.write(workbook);
	}

	private static double[] createColumnWidthPercentages(
			final TableColumnData[] tableColumnDataArray) {

		double widthWeightSum = 0;
		for (final TableColumnData tableColumn : tableColumnDataArray) {

			final double widthWeight = tableColumn.widthWeight();
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

			final String columnName = tableColumnData.name();
			xlsRow.getXlsCellList().add(new XlsCellString(cellStyleTitle, columnName));
		}

		return xlsRow;
	}

	private static void fillRowList(
			final TableRowData tableRowData,
			final Workbook workbook,
			final WriterCellStyles writerCellStyles,
			final List<XlsRow> xlsRowList) {

		final List<List<XlsCell>> xlsCellsByColumnList = new ArrayList<>();
		final DataItem<?>[] dataItemArray = tableRowData.getDataItemArray();
		for (int i = 0; i < dataItemArray.length; i++) {
			xlsCellsByColumnList.add(new ArrayList<>());
		}

		for (int i = 0; i < dataItemArray.length; i++) {

			final DataItem<?> dataItem = dataItemArray[i];

			final short indent;
			if (dataItem != null) {
				indent = dataItem.getIndent();
			} else {
				indent = 0;
			}

			final List<XlsCell> xlsCellList = xlsCellsByColumnList.get(i);

			switch (dataItem) {

				case final DataItemBoolean dataItemBoolean -> {

					final boolean booleanValue = dataItemBoolean.createXlsxValue();

					final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

					final XlsCell xlsCell = new XlsCellBoolean(cellStyle, booleanValue);
					xlsCellList.add(xlsCell);
				}
				case final DataItemUByteHex dataItemUByteHex -> {

					final byte byteValue = dataItemUByteHex.createXlsxValue();
					if (byteValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(byteValue);

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellString(cellStyle, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUByte dataItemUByte -> {

					final byte byteValue = dataItemUByte.createXlsxValue();
					if (byteValue >= 0) {

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellNumber(cellStyle, byteValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUIntHex dataItemUIntHex -> {

					final int integerValue = dataItemUIntHex.createXlsxValue();
					if (integerValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(integerValue);

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellString(cellStyle, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemUInt dataItemUInt -> {

					final int integerValue = dataItemUInt.createXlsxValue();
					if (integerValue >= 0) {

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellNumber(cellStyle, integerValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemULongHex dataItemULongHex -> {

					final long longValue = dataItemULongHex.createXlsxValue();
					if (longValue >= 0) {

						final String hexStringValue = StrUtils.createHexString(longValue);

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellString(cellStyle, hexStringValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemULong dataItemULong -> {

					final long longValue = dataItemULong.createXlsxValue();
					if (longValue >= 0) {

						final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

						final XlsCell xlsCell = new XlsCellNumber(cellStyle, longValue);
						xlsCellList.add(xlsCell);
					}
				}
				case final DataItemDouble dataItemDouble -> {

					final double doubleValue = dataItemDouble.createXlsxValue();
					if (!Double.isNaN(doubleValue)) {

						final CellStyle cellStyle =
								writerCellStyles.computeCellStyleDecimalNumber(workbook, indent);

						final XlsCell xlsCell = new XlsCellNumber(cellStyle, doubleValue);
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

							final CellStyle cellStyle = writerCellStyles.computeCellStyleRegular(workbook, indent);

							final XlsCell xlsCell = new XlsCellString(cellStyle, stringValuePart);
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
					final CellStyle cellStyleRegular = writerCellStyles.getCellStyleRegular();
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
