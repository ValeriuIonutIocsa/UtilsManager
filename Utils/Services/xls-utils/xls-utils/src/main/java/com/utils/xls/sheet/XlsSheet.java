package com.utils.xls.sheet;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.utils.string.StrUtils;
import com.utils.xls.row.XlsRow;

public class XlsSheet {

	private final String name;
	private final double[] columnWidthRatioArray;

	private final List<XlsRow> xlsRowList;

	public XlsSheet(
			final String name,
			final double[] columnWidthRatioArray) {

		this.name = name;
		this.columnWidthRatioArray = columnWidthRatioArray;

		xlsRowList = new ArrayList<>();
	}

	public void write(
			final Workbook workbook) {

		final Sheet sheet = getOrCreateSheet(workbook, name);

		if (columnWidthRatioArray != null) {
			sizeColumns(sheet, columnWidthRatioArray);
		}

		int maxCellCount = 0;
		final int rowCount = xlsRowList.size();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {

			final XlsRow xlsRow = xlsRowList.get(rowIndex);
			xlsRow.write(sheet, rowIndex);

			final int cellCount = xlsRow.getXlsCellList().size();
			maxCellCount = Math.max(maxCellCount, cellCount);
		}
	}

	private static Sheet getOrCreateSheet(
			final Workbook workbook,
			final String name) {

		Sheet sheet = workbook.getSheet(name);
		if (sheet == null) {
			sheet = workbook.createSheet(name);
		}
		return sheet;
	}

	private static void sizeColumns(
			final Sheet sheet,
			final double[] columnWidthRatioArray) {

		for (int columnIndex = 0; columnIndex < columnWidthRatioArray.length; columnIndex++) {

			final double columnWidthRatio = columnWidthRatioArray[columnIndex];
			sheet.setColumnWidth(columnIndex, (int) Math.floor(54_000 * columnWidthRatio));
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public List<XlsRow> getXlsRowList() {
		return xlsRowList;
	}
}
