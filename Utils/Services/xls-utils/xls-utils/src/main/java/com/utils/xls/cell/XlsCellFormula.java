package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class XlsCellFormula extends AbstractXlsCell {

	private final String value;

	public XlsCellFormula(
			final CellStyle style,
			final String value) {

		super(style);

		this.value = value;
	}

	@Override
	public Cell write(
			final Row row,
			final int rowIndex,
			final int cellIndex) {

		final Cell cell = super.write(row, rowIndex, cellIndex);
		cell.setCellFormula(value);
		return cell;
	}
}
