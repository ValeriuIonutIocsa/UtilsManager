package com.utils.xls;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.xls.workbook.WorkbookWrapper;

public final class XlsUtils {

	private XlsUtils() {
	}

	public static WorkbookWrapper createNewWorkbook() {

		final Workbook workbook = new SXSSFWorkbook();
		return new WorkbookWrapper(workbook);
	}

	public static WorkbookWrapper openWorkbook(
			final InputStream inputStream) throws Exception {

		final Workbook workbook = new XSSFWorkbook(inputStream);
		return new WorkbookWrapper(workbook);
	}

	public static void saveWorkbook(
			final Workbook workbook,
			final String pathString) throws Exception {

		FactoryFolderCreator.getInstance().createParentDirectories(pathString, false, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(pathString, false, true);

		try (OutputStream outputStream = StreamUtils.openBufferedOutputStream(pathString)) {

			workbook.write(outputStream);
		}
	}
}
