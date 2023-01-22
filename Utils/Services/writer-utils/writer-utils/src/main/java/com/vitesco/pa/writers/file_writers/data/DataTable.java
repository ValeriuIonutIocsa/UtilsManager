package com.vitesco.pa.writers.file_writers.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.utils.data_types.DataInfo;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

public class DataTable {

	private final String displayName;
	private final String xmlRootElementTagName;
	private final String xmlDataElementTagName;
	private final TableColumnData[] columnsData;
	private final List<? extends TableRowData> rowDataList;

	public DataTable(
			final DataInfo dataInfo,
			final Collection<? extends TableRowData> rowDataList) {
		this(dataInfo, dataInfo.getColumnsData(), rowDataList);
	}

	public DataTable(
			final DataInfo dataInfo,
			final TableColumnData[] columnsData,
			final Collection<? extends TableRowData> rowDataList) {
		this(dataInfo.getDisplayName(), dataInfo.getXmlRootElementTagName(), dataInfo.getXmlDataElementTagName(),
				columnsData, rowDataList);
	}

	public DataTable(
			final String displayName,
			final String xmlRootElementTagName,
			final String xmlDataElementTagName,
			final TableColumnData[] columnsData,
			final Collection<? extends TableRowData> rowDataList) {

		this.displayName = displayName;
		this.xmlRootElementTagName = xmlRootElementTagName;
		this.xmlDataElementTagName = xmlDataElementTagName;
		this.columnsData = columnsData;
		this.rowDataList = new ArrayList<>(rowDataList);
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

	public TableColumnData[] getColumnsData() {
		return columnsData;
	}

	public List<? extends TableRowData> getRowDataList() {
		return rowDataList;
	}
}
