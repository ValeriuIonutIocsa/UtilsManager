package com.utils.srec;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.utils.io.PathUtils;
import com.utils.log.Logger;
import com.utils.test.DynamicTestOption;
import com.utils.test.DynamicTestOptions;
import com.utils.test.DynamicTestSuite;

class SRecFileTest {

	@TestFactory
	List<DynamicTest> testParseAndSave() {

		final DynamicTestOptions<String> sRecFilePathDynamicTestOptions =
				new DynamicTestOptions<>("SREC file path", 1);

		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(1, "0G0M0_000U0_000",
				"D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_000U0_000\\" +
						"_FS_0G0M0_000U0_NORMAL\\out\\code\\FS_0G0M0_000u0_170.S19"));
		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(2, "0G0M0_001U0_000",
				"D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_001U0_000\\" +
						"_FS_0G0M0_001U0_NORMAL\\out\\code\\FS_0G0M0_001u0_170.S19"));
		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(3, "0G0M0_002U0_000",
				"D:\\casdev\\td5\\0g\\0m0\\000\\0G0M0_002U0_000\\" +
						"_FS_0G0M0_002U0_NORMAL\\out\\code\\FS_0G0M0_002u0_170.S19"));

		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(4, "0G0Z0_000U0_000",
				"D:\\casdev\\td5\\0g\\0z0\\000\\0G0Z0_000U0_000" +
						"\\_FS_0G0Z0_000U0_NORMAL\\out\\code\\FS_0G0Z0_000u0_170.S19"));
		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(5, "0G0Z0_001U0_000",
				"D:\\casdev\\td5\\0g\\0z0\\000\\0G0Z0_001U0_000" +
						"\\_FS_0G0Z0_001U0_NORMAL\\out\\code\\FS_0G0Z0_001u0_170.S19"));
		sRecFilePathDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(6, "0G0Z0_002U0_000",
				"D:\\casdev\\td5\\0g\\0z0\\000\\0G0Z0_002U0_000" +
						"\\_FS_0G0Z0_002U0_NORMAL\\out\\code\\FS_0G0Z0_002u0_170.S19"));

		return new DynamicTestSuite(DynamicTestSuite.Mode.ALL, () -> testParseAndSaveCommon(
				sRecFilePathDynamicTestOptions),
				sRecFilePathDynamicTestOptions).createDynamicTestList();
	}

	private static void testParseAndSaveCommon(
			final DynamicTestOptions<String> sRecFilePathDynamicTestOptions) {

		final String sRecFilePathString = sRecFilePathDynamicTestOptions.computeValue();
		try {
			final SRecFile sRecFile = FactorySRecFile.newInstance(sRecFilePathString);
			Assertions.assertNotNull(sRecFile);

			final String copySRecFilePathString =
					PathUtils.appendFileNameSuffix(sRecFilePathString, "_COPY");
			sRecFile.save(copySRecFilePathString);

			final boolean contentEquals =
					FileUtils.contentEquals(new File(sRecFilePathString), new File(copySRecFilePathString));
			Assertions.assertTrue(contentEquals);

		} catch (final Exception exc) {
			Logger.printError("error while parsing and saving SREC file:" +
					System.lineSeparator() + sRecFilePathString);
			Logger.printException(exc);
		}
	}
}
