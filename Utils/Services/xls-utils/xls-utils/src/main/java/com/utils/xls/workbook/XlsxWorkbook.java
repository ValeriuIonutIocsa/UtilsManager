package com.utils.xls.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface XlsxWorkbook {

	static Workbook createNew() {
		return new SXSSFWorkbook();
	}

	boolean parse();
}
