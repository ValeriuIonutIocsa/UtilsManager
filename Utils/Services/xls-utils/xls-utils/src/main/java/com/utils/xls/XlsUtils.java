package com.utils.xls;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Workbook;

import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;

public final class XlsUtils {

	private XlsUtils() {
	}

	public static void saveWorkbook(
			final Workbook workbook,
			final Path path) throws Exception {

		FactoryFolderCreator.getInstance().createParentDirectories(path, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(path, true);

		try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(path))) {

			workbook.write(outputStream);
		}
	}
}
