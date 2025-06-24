package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.utils.log.Logger;
import com.utils.xls.cell.regions.Region;

public abstract class AbstractXlsCell implements XlsCell {

	private final CellStyle style;

	private Region[] regionArray;

	AbstractXlsCell(
			final CellStyle style) {

		this.style = style;
	}

	@Override
	public Cell write(
			final Row row,
			final int rowIndex,
			final int cellIndex) {

		final Cell cell = getOrCreateCell(row, cellIndex);
		if (style != null) {
			cell.setCellStyle(style);
		}

		if (regionArray != null) {

			for (final Region region : regionArray) {

				try {
					final Sheet sheet = row.getSheet();
					region.write(sheet, rowIndex, cellIndex);

				} catch (final Throwable throwable) {
					Logger.printError("failed to write region to Excel");
					Logger.printThrowable(throwable);
				}
			}
		}

		return cell;
	}

	private static Cell getOrCreateCell(
			final Row row,
			final int cellIndex) {

		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			cell = row.createCell(cellIndex);
		}
		return cell;
	}

	@Override
	public XlsCell configureRegionArray(
			final Region... regionArray) {

		this.regionArray = regionArray;
		return this;
	}
}
