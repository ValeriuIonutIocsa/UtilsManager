package com.utils.writers.file_writers;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.utils.xls.style.FactoryXlsCellStyles;

class WriterCellStyles {

	private CellStyle cellStyleTitle;
	private CellStyle cellStyleRegular;
	private CellStyle cellStyleDecimalNumber;

	private final Map<Short, CellStyle> indentToCellStyleRegularMap;
	private final Map<Short, CellStyle> indentToCellStyleDecimalNumberMap;

	WriterCellStyles() {

		indentToCellStyleRegularMap = new HashMap<>();
		indentToCellStyleDecimalNumberMap = new HashMap<>();
	}

	void initialize(
			final Workbook workbook) {

		cellStyleTitle = FactoryXlsCellStyles.createCellStyleTitle(workbook);

		cellStyleRegular = FactoryXlsCellStyles.createCellStyleRegular(workbook);
		cellStyleDecimalNumber = FactoryXlsCellStyles.createCellStyleDecimalNumber(workbook);
	}

	CellStyle computeCellStyleRegular(
			final Workbook workbook,
			final short indent) {

		CellStyle cellStyle;
		if (indent > 0) {

			cellStyle = indentToCellStyleRegularMap.get(indent);
			if (cellStyle == null) {

				cellStyle = FactoryXlsCellStyles.createCellStyleRegular(workbook);
				cellStyle.setIndention(indent);
				indentToCellStyleRegularMap.put(indent, cellStyle);
			}

		} else {
			cellStyle = cellStyleRegular;
		}
		return cellStyle;
	}

	CellStyle computeCellStyleDecimalNumber(
			final Workbook workbook,
			final short indent) {

		CellStyle cellStyle;
		if (indent > 0) {

			cellStyle = indentToCellStyleDecimalNumberMap.get(indent);
			if (cellStyle == null) {

				cellStyle = FactoryXlsCellStyles.createCellStyleDecimalNumber(workbook);
				cellStyle.setIndention(indent);
				indentToCellStyleDecimalNumberMap.put(indent, cellStyle);
			}

		} else {
			cellStyle = cellStyleDecimalNumber;
		}
		return cellStyle;
	}

	CellStyle getCellStyleTitle() {
		return cellStyleTitle;
	}

	CellStyle getCellStyleRegular() {
		return cellStyleRegular;
	}
}
