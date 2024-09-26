package com.utils.writers.file_writers;

import java.util.List;

import com.utils.writers.file_writers.data.DataTable;

public interface DataFileWriter {

	String write(
			String displayName,
			String outputPathString,
			List<DataTable> dataTableList) throws Exception;

	String getExtension();

	int getOrder();
}
