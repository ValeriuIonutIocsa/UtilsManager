package com.utils.writers.file_writers;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.log.Logger;
import com.utils.writers.file_writers.data.DataTable;
import com.utils.writers.file_writers.data.DataTableTestUtils;

class DataFileWriterXlsxTest {

	@Test
	void testWriteData() throws Exception {

		final String outputFilePathString = PathUtils.computePath(PathUtils.createRootPath(),
				"IVI", "Tmp", "writer-utils-xlsx", "test.xlsx");
		Logger.printProgress("writing output file:");
		Logger.printLine(outputFilePathString);

		final DataTable testDataTable = DataTableTestUtils.createTestDataTable();
		final List<DataTable> dataTableList = List.of(testDataTable);
		DataFileWriterXlsx.INSTANCE.writeData("write XLSX test", outputFilePathString, dataTableList);

		final String copyOutputFilePathString = PathUtils.appendFileNameSuffix(outputFilePathString, "_COPY");
		FactoryFileCopier.getInstance()
				.copyFile(outputFilePathString, copyOutputFilePathString, true, true, true);
	}
}
