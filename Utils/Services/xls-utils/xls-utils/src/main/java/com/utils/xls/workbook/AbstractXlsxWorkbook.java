package com.utils.xls.workbook;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractXlsxWorkbook implements XlsxWorkbook {

	private final Path path;

	protected AbstractXlsxWorkbook(
			final Path path) {

		this.path = path;
	}

	@Override
	public boolean parse() {

		boolean success = false;
		try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(path));
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
