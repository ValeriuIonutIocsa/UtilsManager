package com.vitesco.pa.writers.file_writers;

import java.util.List;

import com.vitesco.pa.writers.file_writers.data.DataTable;

public interface DataFileWriter {

	void write(
			String displayName,
			String outputPathString,
			List<DataTable> dataTableList) throws Exception;
}
