package com.utils.writers.file_writers.data;

import java.io.PrintStream;
import java.util.List;

import com.utils.json.JsonUtils;
import com.utils.string.StrUtils;

class JsonSecondLevelRoot {

	private final List<DataTable> dataTableList;

	JsonSecondLevelRoot(
			final List<DataTable> dataTableList) {

		this.dataTableList = dataTableList;
	}

	void writeToJson(
			final int indentCount,
			final PrintStream printStream) {

		for (int i = 0; i < dataTableList.size(); i++) {

			final DataTable dataTable = dataTableList.get(i);
			final String xmlRootElementTagName = dataTable.getXmlRootElementTagName();
			final boolean notLastAttribute = i < dataTableList.size() - 1;
			JsonUtils.writeObjectAttribute(xmlRootElementTagName, dataTable, notLastAttribute,
					indentCount, printStream, DataTable::writeToJson);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
