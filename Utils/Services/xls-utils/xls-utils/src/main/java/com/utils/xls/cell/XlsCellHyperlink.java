package com.utils.xls.cell;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class XlsCellHyperlink extends AbstractXlsCell {

	private final String value;

	private final String sheetName;
	private final String cellName;

	public XlsCellHyperlink(
			final CellStyle style,
			final String value,
			final String sheetName,
			final String cellName) {

		super(style);

		this.value = value;

		this.sheetName = sheetName;
		this.cellName = cellName;
	}

	@Override
	public Cell write(
			final Row row,
			final int rowIndex,
			final int cellIndex) {

		final Cell cell = super.write(row, rowIndex, cellIndex);

		cell.setCellValue(value);

		final Workbook workbook = row.getSheet().getWorkbook();
		final Hyperlink hyperlink = workbook.getCreationHelper()
				.createHyperlink(HyperlinkType.DOCUMENT);
		final String hyperlinkAddress = "'" + sheetName + "'!" + cellName;
		hyperlink.setAddress(hyperlinkAddress);
		cell.setHyperlink(hyperlink);

		return cell;
	}
}
