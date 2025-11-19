package com.utils.xls.workbook;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.utils.io.StreamUtils;
import com.utils.xls.XlsUtils;

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
				WorkbookWrapper workbookWrapper = XlsUtils.openWorkbook(inputStream)) {

			final Workbook workbook = workbookWrapper.getWorkbook();
			parseWorkbook(workbook);
			success = true;

		} catch (final Throwable throwable) {
			handleError(throwable);
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

			} catch (final Throwable ignored) {
			}
		}
	}

	protected abstract void handleError(
			Throwable throwable);
}
