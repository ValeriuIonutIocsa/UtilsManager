package com.utils.xml;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AppStartXmlObfuscatorTest {

	@Test
	void testWork() {

		final String cnfFilePathString = "cfg\\XmlObfuscatorCfg.xml";

		final Path cnfFilePath = Paths.get(cnfFilePathString);
		AppStartXmlObfuscator.main(cnfFilePath);
	}
}
