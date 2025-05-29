package com.utils.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class YamlUtilsTest {

	@Test
	void testParseYamlResourceFile() {

		final Person person =
				YamlUtils.parseYamlResourceFile("com/utils/yaml/person.yaml", Person.class);
		Assertions.assertNotNull(person);

		Logger.printNewLine();
		Logger.printLine(person);
	}
}
