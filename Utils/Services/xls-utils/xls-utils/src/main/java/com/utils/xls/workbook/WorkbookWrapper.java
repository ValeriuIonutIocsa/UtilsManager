package com.utils.xls.workbook;

import java.io.Closeable;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

import com.utils.tmp.TmpFolderUtils;

public class WorkbookWrapper implements Closeable {

	private final Workbook workbook;

	private final String originalJavaTmpDir;

	public WorkbookWrapper(
			final Workbook workbook) {

		this.workbook = workbook;

		originalJavaTmpDir = System.getProperty("java.io.tmpdir");

		final String tmpFolderPathString = TmpFolderUtils.createTmpFolderPathString();
		System.setProperty("java.io.tmpdir", tmpFolderPathString);
	}

	@Override
	public void close() throws IOException {

		workbook.close();

		System.setProperty("java.io.tmpdir", originalJavaTmpDir);
	}

	public Workbook getWorkbook() {
		return workbook;
	}
}
