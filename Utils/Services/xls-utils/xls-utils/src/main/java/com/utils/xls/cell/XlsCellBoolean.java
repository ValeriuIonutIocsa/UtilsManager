package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class XlsCellBoolean extends AbstractXlsCell {

	private final boolean value;

	public XlsCellBoolean(
			final CellStyle style,
			final boolean value) {

		super(style);

		this.value = value;
	}

	@Override
	public Cell write(
			final Row row,
			final int rowIndex,
			final int cellIndex) {

		final Cell cell = super.write(row, rowIndex, cellIndex);
		cell.setCellValue(value);
		return cell;
	}
}
