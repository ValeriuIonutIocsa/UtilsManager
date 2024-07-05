package com.utils.writers.file_writers.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.utils.data_types.DataInfo;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.json.JsonUtils;
import com.utils.string.StrUtils;

public class DataTable {

	private final String displayName;
	private final String xmlRootElementTagName;
	private final String xmlDataElementTagName;
	private final int totalColumnWidth;
	private final TableColumnData[] columnsData;
	private final List<? extends TableRowData> rowDataList;

	public DataTable(
			final DataInfo dataInfo,
			final Collection<? extends TableRowData> rowDataList) {

		this(dataInfo, dataInfo.totalColumnWidth(), dataInfo.columnsData(), rowDataList);
	}

	public DataTable(
			final DataInfo dataInfo,
			final int totalColumnWidth,
			final TableColumnData[] columnsData,
			final Collection<? extends TableRowData> rowDataList) {

		this(dataInfo.displayName(), dataInfo.xmlRootElementTagName(), dataInfo.xmlDataElementTagName(),
				totalColumnWidth, columnsData, rowDataList);
	}

	public DataTable(
			final String displayName,
			final String xmlRootElementTagName,
			final String xmlDataElementTagName,
			final int totalColumnWidth,
			final TableColumnData[] columnsData,
			final Collection<? extends TableRowData> rowDataList) {

		this.displayName = displayName;
		this.xmlRootElementTagName = xmlRootElementTagName;
		this.xmlDataElementTagName = xmlDataElementTagName;
		this.totalColumnWidth = totalColumnWidth;
		this.columnsData = columnsData;
		this.rowDataList = new ArrayList<>(rowDataList);
	}

	public void writeToJson(
			final int indentCount,
			final PrintStream printStream) {

		JsonUtils.writeStringAttribute("Name", displayName, true, indentCount, printStream);

		JsonUtils.writeListAttribute(xmlDataElementTagName, rowDataList, false,
				indentCount, printStream, (
						tableRowDataArg,
						indentCountArg,
						printStreamArg) -> tableRowDataArg.writeToJson(
								columnsData, indentCountArg, printStreamArg));
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getXmlRootElementTagName() {
		return xmlRootElementTagName;
	}

	public String getXmlDataElementTagName() {
		return xmlDataElementTagName;
	}

	public int getTotalColumnWidth() {
		return totalColumnWidth;
	}

	public TableColumnData[] getColumnsData() {
		return columnsData;
	}

	public List<? extends TableRowData> getRowDataList() {
		return rowDataList;
	}
}
