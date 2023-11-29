package com.utils.xls.workbook;

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
			excelFilePathString = "D:\\VT_IVI_MISC\\Tmp\\ADV\\ManifConnector\\ConnectionReport.xlsx";
		} else {
			throw new RuntimeException();
		}

		final boolean success = new AbstractXlsxWorkbook(excelFilePathString) {

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
