package com.utils.xls.cell;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class XlsCellString extends AbstractXlsCell {

	private final String value;

	public XlsCellString(
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
		final String value;
		if (StringUtils.length(this.value) > 32_000) {
			value = "too long text...";
		} else {
			value = this.value;
		}
		cell.setCellValue(value);
		return cell;
	}
}
