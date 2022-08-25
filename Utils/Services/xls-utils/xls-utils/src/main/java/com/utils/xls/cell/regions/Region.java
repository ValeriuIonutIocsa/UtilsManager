package com.utils.xls.cell.regions;

import org.apache.poi.ss.usermodel.Sheet;

public interface Region {

	void write(
			Sheet sheet,
			int firstRow,
			int firstCol) throws Exception;
}
