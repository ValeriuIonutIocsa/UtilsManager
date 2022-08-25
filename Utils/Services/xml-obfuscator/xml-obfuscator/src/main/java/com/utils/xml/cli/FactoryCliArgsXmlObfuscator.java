package com.utils.xml.cli;

import java.nio.file.Path;
import java.nio.file.Paths;

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

				final String configurationFilePathString = args[0];
				final Path configurationFilePath =
						Paths.get(configurationFilePathString).toAbsolutePath();
				cliArgsXmlObfuscator = new CliArgsXmlObfuscator(configurationFilePath);
			}

		} catch (final Exception exc) {
			Logger.printError("invalid command line arguments!");
			Logger.printException(exc);
		}
		return cliArgsXmlObfuscator;
	}
}
