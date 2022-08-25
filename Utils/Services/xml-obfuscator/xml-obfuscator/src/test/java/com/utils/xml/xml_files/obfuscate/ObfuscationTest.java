package com.utils.xml.xml_files.obfuscate;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ObfuscationTest {

	@Test
	void testObfuscateText() {

		final String obfuscatedText =
				Obfuscation.obfuscateText("function_name?type=Label", new HashMap<>());
		Assertions.assertEquals("val_0?type=Label", obfuscatedText);
	}
}
