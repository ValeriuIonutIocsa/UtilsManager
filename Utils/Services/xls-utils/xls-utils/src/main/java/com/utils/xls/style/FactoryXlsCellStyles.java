package com.utils.xls.style;

import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.utils.log.Logger;

public final class FactoryXlsCellStyles {

	private FactoryXlsCellStyles() {
	}

	public static CellStyle createCellStyleBigTitle(
			final Workbook workbook) {

		final CellStyle cellStyleTitle = workbook.createCellStyle();

		cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
		cellStyleTitle.setWrapText(true);

		final Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBold(true);
		cellStyleTitle.setFont(font);

		return cellStyleTitle;
	}

	public static CellStyle createCellStyleTitle(
			final Workbook workbook) {

		final CellStyle cellStyleTitle = workbook.createCellStyle();

		cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
		cellStyleTitle.setWrapText(true);

		final Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setBold(true);
		cellStyleTitle.setFont(font);

		return cellStyleTitle;
	}

	public static CellStyle createCellStyleCenter(
			final Workbook workbook) {

		final CellStyle cellStyleCenter = workbook.createCellStyle();

		cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
		cellStyleCenter.setWrapText(true);

		return cellStyleCenter;
	}

	public static CellStyle createCellStyleCenterLeft(
			final Workbook workbook) {

		final CellStyle cellStyleCenterLeft = workbook.createCellStyle();

		cellStyleCenterLeft.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyleCenterLeft.setAlignment(HorizontalAlignment.LEFT);
		cellStyleCenterLeft.setWrapText(true);

		return cellStyleCenterLeft;
	}

	public static CellStyle createCellStyleRegular(
			final Workbook workbook) {

		final CellStyle cellStyleRegular = workbook.createCellStyle();

		cellStyleRegular.setAlignment(HorizontalAlignment.LEFT);
		cellStyleRegular.setWrapText(true);

		return cellStyleRegular;
	}

	public static CellStyle createCellStyleDecimalNumber(
			final Workbook workbook) {

		final CellStyle cellStyleDecimalNumber = workbook.createCellStyle();
		cellStyleDecimalNumber.setAlignment(HorizontalAlignment.LEFT);
		cellStyleDecimalNumber.setDataFormat(workbook.createDataFormat()
				.getFormat("[<0.001]0.000#;[<0.01]0.00#;0.00"));
		return cellStyleDecimalNumber;
	}

	public static CellStyle createHyperlinkCellStyle(
			final Workbook workbook) {

		final CellStyle cellStyleHyperlink = workbook.createCellStyle();

		cellStyleHyperlink.setAlignment(HorizontalAlignment.LEFT);
		cellStyleHyperlink.setWrapText(true);

		final Font fontHyperlink = workbook.createFont();
		fontHyperlink.setUnderline(Font.U_SINGLE);
		fontHyperlink.setColor(IndexedColors.BLUE.getIndex());
		cellStyleHyperlink.setFont(fontHyperlink);

		return cellStyleHyperlink;
	}

	public static CellStyle createCellStyleColored(
			final Workbook workbook,
			final String colorRgb) {

		final CellStyle cellStyleColored = workbook.createCellStyle();

		cellStyleColored.setAlignment(HorizontalAlignment.LEFT);
		cellStyleColored.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyleColored.setWrapText(true);

		try {
			if (cellStyleColored instanceof final XSSFCellStyle xssfCellStyle) {

				final XSSFColor xssfColor = new XSSFColor(Hex.decodeHex(colorRgb), null);
				xssfCellStyle.setFillForegroundColor(xssfColor);
			}
			cellStyleColored.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		} catch (final Exception exc) {
			Logger.printError("failed to set Excel cell color");
			Logger.printException(exc);
		}

		return cellStyleColored;
	}
}
