package com.utils.xml;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AppStartXmlObfuscatorTest {

	@Test
	void testWork() {

		String cnfFilePathString = "cfg\\XmlObfuscatorCfg.xml";
		cnfFilePathString = Paths.get(cnfFilePathString).toString();
		AppStartXmlObfuscator.main(cnfFilePathString);
	}
}
