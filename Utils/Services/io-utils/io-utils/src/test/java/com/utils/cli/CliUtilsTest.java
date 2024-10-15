package com.utils.cli;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class CliUtilsTest {

	@Test
	void testCommandStringToArray() {

		final String commandString;
		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {
			commandString = "cmd /c \"C:\\IVI\\Vitesco\\Prog\\JavaGradle\\CRO\\" +
					"ProjectAnalyzer\\Projects\\CRO\\SpaBackend\\SpaBackend\\" +
					"timed_event_hook\\timed event hook script.bat\"";
		} else {
			throw new RuntimeException();
		}

		final String[] commandPartArray = CliUtils.commandStringToArray(commandString);
		Assertions.assertNotNull(commandPartArray);

		Logger.printNewLine();
		Logger.printLine(StringUtils.join(commandPartArray, " "));
	}
}
