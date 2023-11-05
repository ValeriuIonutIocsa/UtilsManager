package com.vitesco.pa.writers.file_writers.data;

import java.io.PrintStream;
import java.util.List;

import com.utils.json.JsonUtils;
import com.utils.string.StrUtils;

public class JsonFirstLevelRoot {

	private final List<DataTable> dataTableList;

	public JsonFirstLevelRoot(
			final List<DataTable> dataTableList) {

		this.dataTableList = dataTableList;
	}

	public void writeToJson(
			final int indentCount,
			final PrintStream printStream) {

		final JsonSecondLevelRoot jsonSecondLevelRoot = new JsonSecondLevelRoot(dataTableList);
		JsonUtils.writeObjectAttribute("ExportedData", jsonSecondLevelRoot, false,
				indentCount, printStream, JsonSecondLevelRoot::writeToJson);
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
