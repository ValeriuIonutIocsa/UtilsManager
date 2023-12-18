package com.utils.srec;

import org.junit.jupiter.api.Test;

import com.utils.string.StrUtils;

class SRecUtilsTest {

	@Test
	void testComparePatchedWithOriginal() {

		final String patchDataCsvPathString;
		final String outputSRecFilePathString;
		final String sRecFilePathString;
		final int input = StrUtils.tryParsePositiveInt("1");
		if (input == 1) {
			patchDataCsvPathString = "D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_000U0_000\\" +
					"_FS_0G0M0_000U0_NORMAL\\out\\code\\FS_0G0M0_000U0_170_MDB_CON.s19.csv";
			outputSRecFilePathString = "D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_000U0_000\\" +
					"_FS_0G0M0_000U0_NORMAL\\out\\code\\FS_0G0M0_000U0_170_MDB_CON.s19";
			sRecFilePathString = "D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_000U0_000\\" +
					"_FS_0G0M0_000U0_NORMAL\\out\\code\\FS_0G0M0_000U0_170.s19";

		} else {
			throw new RuntimeException();
		}
		SRecUtils.comparePatchedWithOriginal(
				patchDataCsvPathString, outputSRecFilePathString, sRecFilePathString);
	}
}
