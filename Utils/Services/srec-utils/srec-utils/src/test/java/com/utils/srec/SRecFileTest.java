package com.utils.srec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;

class SRecFileTest {

	@Test
	void testParseAndSave() {

		final String sRecFilePathString;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			sRecFilePathString = "D:\\docs\\ManifConnector" +
					"\\new_manifest\\host\\out\\code\\FS_0G0A4_0u0_120.S19";
		} else {
			throw new RuntimeException();
		}

		final SRecFile sRecFile = FactorySRecFile.newInstance(sRecFilePathString);
		Assertions.assertNotNull(sRecFile);

		final String sRecFilePathStringWoExt = PathUtils.computePathWoExt(sRecFilePathString);
		final String extension = PathUtils.computeExtension(sRecFilePathString);
		final String copySRecFilePathString = sRecFilePathStringWoExt + "_COPY." + extension;
		sRecFile.save(copySRecFilePathString);
	}
}
