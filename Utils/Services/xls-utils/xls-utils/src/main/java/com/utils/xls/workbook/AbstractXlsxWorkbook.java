package com.utils.xls.workbook;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.utils.io.StreamUtils;

public abstract class AbstractXlsxWorkbook implements XlsxWorkbook {

	private final String pathString;

	protected AbstractXlsxWorkbook(
			final String pathString) {

		this.pathString = pathString;
	}

	@Override
	public boolean parse() {

		boolean success = false;
		try (InputStream inputStream = StreamUtils.openBufferedInputStream(pathString);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			parseWorkbook(workbook);
			success = true;

		} catch (final Exception exc) {
			handleError(exc);
		}
		return success;
	}

	protected abstract void parseWorkbook(
			Workbook workbook);

	protected static void fillColumnTitleByIndexMap(
			final Row row,
			final Map<Integer, String> columnTitleByIndexMap) {

		final Iterator<Cell> cellIterator = row.cellIterator();
		int columnIndex = 0;
		while (cellIterator.hasNext()) {

			columnIndex++;
			final Cell cell = cellIterator.next();
			try {
				final String stringCellValue = cell.getStringCellValue().trim();
				columnTitleByIndexMap.put(columnIndex, stringCellValue);

			} catch (final Exception ignored) {
			}
		}
	}

	protected abstract void handleError(
			Exception exc);
}
