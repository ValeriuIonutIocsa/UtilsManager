package com.utils.xls.cell.regions;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class RegionMerged extends AbstractRegion {

	private final int rowCount;
	private final int colCount;

	public RegionMerged(
			final int rowCount,
			final int colCount) {

		this.rowCount = rowCount;
		this.colCount = colCount;
	}

	@Override
	public void write(
			final Sheet sheet,
			final int firstRow,
			final int firstCol) {

		final int lastRow = firstRow + rowCount - 1;
		final int lastCol = firstCol + colCount - 1;
		final CellRangeAddress cellRangeAddress =
				new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(cellRangeAddress);
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}
}
