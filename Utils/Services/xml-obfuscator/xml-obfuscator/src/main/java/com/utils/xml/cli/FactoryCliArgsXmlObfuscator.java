package com.utils.xml.cli;

import com.utils.io.PathUtils;
import com.utils.log.Logger;

public final class FactoryCliArgsXmlObfuscator {

	private FactoryCliArgsXmlObfuscator() {
	}

	public static CliArgsXmlObfuscator newInstance(
			final String[] args) {

		CliArgsXmlObfuscator cliArgsXmlObfuscator = null;
		try {
			if (args.length != 1) {
				Logger.printError("usage: java -jar XmlObfuscator.jar CONFIGURATION_FILE_PATH");

			} else {

				String configurationFilePathString = args[0];
				configurationFilePathString = PathUtils.computePath(configurationFilePathString);
				cliArgsXmlObfuscator = new CliArgsXmlObfuscator(configurationFilePathString);
			}

		} catch (final Exception exc) {
			Logger.printError("invalid command line arguments");
			Logger.printException(exc);
		}
		return cliArgsXmlObfuscator;
	}
}
