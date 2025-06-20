package com.utils.xls;

import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;

public final class XlsUtils {

	private XlsUtils() {
	}

	public static Workbook createNewWorkbook() {

		return new SXSSFWorkbook();
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
