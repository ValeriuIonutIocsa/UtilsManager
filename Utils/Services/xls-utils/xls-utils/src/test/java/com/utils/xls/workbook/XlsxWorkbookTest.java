package com.utils.xls.workbook;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class XlsxWorkbookTest {

	@Test
	void testParse() {

		final String excelFilePathString;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			excelFilePathString = "D:\\docs\\ManifConnector\\ConnectionReport.xlsx";
		} else {
			throw new RuntimeException();
		}

		final Path excelFilePath = Paths.get(excelFilePathString);
		final boolean success = new AbstractXlsxWorkbook(excelFilePath) {

			@Override
			protected void parseWorkbook(
					final Workbook workbook) {

				final int numberOfSheets = workbook.getNumberOfSheets();
				Logger.printLine("sheet count: " + numberOfSheets);
			}

			@Override
			protected void handleError(
					final Exception exc) {

				Logger.printError("failed to open the workbook");
				Logger.printException(exc);
			}

		}.parse();
		Assertions.assertTrue(success);
	}
}
