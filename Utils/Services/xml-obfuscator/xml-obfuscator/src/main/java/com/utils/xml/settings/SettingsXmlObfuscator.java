package com.utils.xml.settings;

import java.util.List;

import com.utils.string.StrUtils;
import com.utils.xml.xml_files.XmlFile;

public class SettingsXmlObfuscator {

	private final List<XmlFile> xmlFileList;

	SettingsXmlObfuscator(
			final List<XmlFile> xmlFileList) {

		this.xmlFileList = xmlFileList;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public List<XmlFile> getXmlFileList() {
		return xmlFileList;
	}
}
