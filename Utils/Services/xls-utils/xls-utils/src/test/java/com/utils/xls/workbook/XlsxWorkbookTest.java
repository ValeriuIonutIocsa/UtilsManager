package com.utils.xls.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.StrUtils;

class XlsxWorkbookTest {

	@Test
	void testParse() {

		final String excelFilePathString;
		final int input = StrUtils.tryParsePositiveInt("1");
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
					final Throwable throwable) {

				Logger.printError("failed to open the workbook");
				Logger.printThrowable(throwable);
			}

		}.parse();
		Assertions.assertTrue(success);
	}
}
