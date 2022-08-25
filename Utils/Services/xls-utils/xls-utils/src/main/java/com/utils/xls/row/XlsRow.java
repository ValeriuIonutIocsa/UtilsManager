package com.utils.xls.row;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.utils.xls.cell.XlsCell;

public class XlsRow {

	private final float height;
	private final List<XlsCell> xlsCellList;

	public XlsRow() {

		this(-1.0f);
	}

	public XlsRow(
			final float height) {

		this.height = height;

		xlsCellList = new ArrayList<>();
	}

	public void write(
			final Sheet sheet,
			final int rowIndex) {

		final Row row = getOrCreateRow(sheet, rowIndex);
		if (height > 0.0f) {
			row.setHeightInPoints(height);
		}

		final int cellCount = xlsCellList.size();
		for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {

			final XlsCell xlsCell = xlsCellList.get(cellIndex);
			xlsCell.write(row, rowIndex, cellIndex);
		}
	}

	private static Row getOrCreateRow(
			final Sheet sheet,
			final int rowIndex) {

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}

	public List<XlsCell> getXlsCellList() {
		return xlsCellList;
	}
}
