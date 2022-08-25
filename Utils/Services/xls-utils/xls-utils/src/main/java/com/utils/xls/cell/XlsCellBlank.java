package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class XlsCellBlank extends AbstractXlsCell {

	public XlsCellBlank(
			final CellStyle style) {
		super(style);
	}

	@Override
	public Cell write(
			final Row row,
			final int rowIndex,
			final int cellIndex) {

		return super.write(row, rowIndex, cellIndex);
	}
}
