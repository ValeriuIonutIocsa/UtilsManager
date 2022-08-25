package com.utils.xml.cli;

import java.nio.file.Path;

public class CliArgsXmlObfuscator {

	private final Path configurationFilePath;

	CliArgsXmlObfuscator(
			final Path configurationFilePath) {

		this.configurationFilePath = configurationFilePath;
	}

	public Path getConfigurationFilePath() {
		return configurationFilePath;
	}
}
