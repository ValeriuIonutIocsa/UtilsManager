package com.utils.xls.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.utils.xls.cell.regions.Region;

public interface XlsCell {

	Cell write(
			Row row,
			int rowIndex,
			int cellIndex);

	XlsCell configureRegionArray(
			Region... regionArray);
}
