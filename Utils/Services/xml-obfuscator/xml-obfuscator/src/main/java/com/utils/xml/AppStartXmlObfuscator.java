package com.utils.xml;

import java.nio.file.Path;
import java.util.List;

import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.xml.cli.CliArgsXmlObfuscator;
import com.utils.xml.cli.FactoryCliArgsXmlObfuscator;
import com.utils.xml.settings.FactorySettingsXmlObfuscator;
import com.utils.xml.settings.SettingsXmlObfuscator;
import com.utils.xml.xml_files.XmlFile;

public final class AppStartXmlObfuscator {

	private AppStartXmlObfuscator() {
	}

	public static void main(
			final String[] args) {

		final CliArgsXmlObfuscator cliArgsXmlObfuscator =
				FactoryCliArgsXmlObfuscator.newInstance(args);
		if (cliArgsXmlObfuscator != null) {

			final String cnfFilePathString = cliArgsXmlObfuscator.getConfigurationFilePathString();
			main(cnfFilePathString);
		}
	}

	public static void main(
			final String cnfFilePathString) {

		if (!IoUtils.fileExists(cnfFilePathString)) {
			Logger.printError("the configuration file path is invalid");

		} else {
			final SettingsXmlObfuscator settingsXmlObfuscator =
					FactorySettingsXmlObfuscator.newInstance(cnfFilePathString);
			if (settingsXmlObfuscator != null) {

				final List<XmlFile> xmlFileList = settingsXmlObfuscator.getXmlFileList();
				for (final XmlFile xmlFile : xmlFileList) {
					xmlFile.obfuscate();
				}
			}
		}
	}
}
