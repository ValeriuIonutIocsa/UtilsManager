package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class XlsCellNumber extends AbstractXlsCell {

	private final double value;

	public XlsCellNumber(
			final CellStyle style,
			final double value) {

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
