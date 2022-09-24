package com.utils.xml.cli;

public class CliArgsXmlObfuscator {

	private final String configurationFilePathString;

	CliArgsXmlObfuscator(
			final String configurationFilePathString) {

		this.configurationFilePathString = configurationFilePathString;
	}

	public String getConfigurationFilePathString() {
		return configurationFilePathString;
	}
}
