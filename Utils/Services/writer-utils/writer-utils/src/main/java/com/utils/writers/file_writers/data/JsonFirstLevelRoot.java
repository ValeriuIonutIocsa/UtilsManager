package com.utils.writers.file_writers.data;

import java.util.List;

import com.utils.json.JsonWriter;
import com.utils.string.StrUtils;

public class JsonFirstLevelRoot {

	private final List<DataTable> dataTableList;

	public JsonFirstLevelRoot(
			final List<DataTable> dataTableList) {

		this.dataTableList = dataTableList;
	}

	public void writeToJson(
			final int indentCount,
			final JsonWriter jsonWriter) {

		final JsonSecondLevelRoot jsonSecondLevelRoot = new JsonSecondLevelRoot(dataTableList);
		jsonWriter.writeObjectAttribute("ExportedData", jsonSecondLevelRoot, false,
				indentCount, JsonSecondLevelRoot::writeToJson);
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
