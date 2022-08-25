package com.utils.xls.cell.regions;

import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.ColorScaleFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class RegionTwoPointColorScale extends AbstractRegion {

	private final int rowCount;
	private final int colCount;
	private final String firstColorRgb;
	private final String secondColorRgb;

	public RegionTwoPointColorScale(
			final int rowCount,
			final int colCount,
			final String firstColorRgb,
			final String secondColorRgb) {

		this.rowCount = rowCount;
		this.colCount = colCount;
		this.firstColorRgb = firstColorRgb;
		this.secondColorRgb = secondColorRgb;
	}

	@Override
	public void write(
			final Sheet sheet,
			final int firstRow,
			final int firstCol) throws Exception {

		final ConditionalFormattingRule conditionalFormattingRule =
				sheet.getSheetConditionalFormatting().createConditionalFormattingColorScaleRule();
		final ColorScaleFormatting colorScaleFormatting =
				conditionalFormattingRule.getColorScaleFormatting();

		colorScaleFormatting.setNumControlPoints(2);

		colorScaleFormatting.setColors(new Color[] {
				new XSSFColor(Hex.decodeHex(firstColorRgb), null),
				new XSSFColor(Hex.decodeHex(secondColorRgb), null)
		});

		final ConditionalFormattingThreshold lowConditionalFormattingThreshold =
				colorScaleFormatting.getThresholds()[0];
		lowConditionalFormattingThreshold.setRangeType(
				ConditionalFormattingThreshold.RangeType.MIN);
		final ConditionalFormattingThreshold highConditionalFormattingThreshold =
				colorScaleFormatting.getThresholds()[0];
		highConditionalFormattingThreshold.setRangeType(
				ConditionalFormattingThreshold.RangeType.MAX);
		colorScaleFormatting.setThresholds(new ConditionalFormattingThreshold[] {
				lowConditionalFormattingThreshold,
				highConditionalFormattingThreshold
		});

		final int lastRow = firstRow + rowCount - 1;
		final int lastCol = firstCol + colCount - 1;
		final CellRangeAddress[] cellRangeAddressArray = {
				new CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
		};
		sheet.getSheetConditionalFormatting().addConditionalFormatting(
				cellRangeAddressArray, conditionalFormattingRule);
	}
}
