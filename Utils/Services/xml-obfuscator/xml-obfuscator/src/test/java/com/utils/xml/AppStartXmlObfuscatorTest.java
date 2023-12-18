package com.utils.xml;

import org.junit.jupiter.api.Test;

class AppStartXmlObfuscatorTest {

	@Test
	void testWork() {

		final String cnfFilePathString = "cfg\\XmlObfuscatorCfg.xml";
		AppStartXmlObfuscator.mainL2(cnfFilePathString);
	}
}
