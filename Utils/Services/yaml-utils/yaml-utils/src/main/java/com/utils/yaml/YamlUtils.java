package com.utils.yaml;

import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.io.ResourceFileUtils;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;

public final class YamlUtils {

	private YamlUtils() {
	}

	@ApiMethod
	public static <
			ObjectT> ObjectT parseYamlFile(
					final String yamlFilePathString,
					final Class<ObjectT> objectClass) {

		ObjectT object = null;
		try {
			Logger.printProgress("parsing YAML file:");
			Logger.printLine(yamlFilePathString);

			if (!IoUtils.fileExists(yamlFilePathString)) {
				Logger.printError("YAML file does not exist:" +
						System.lineSeparator() + yamlFilePathString);

			} else {
				final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				try (InputStream inputStream = StreamUtils.openBufferedInputStream(yamlFilePathString)) {

					object = mapper.readValue(inputStream, objectClass);
				}
			}

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
			Logger.printError("failed to parse YAML file:" +
					System.lineSeparator() + yamlFilePathString);
		}
		return object;
	}

	@ApiMethod
	public static <
			ObjectT> ObjectT parseYamlResourceFile(
					final String yamlResourceFilePathString,
					final Class<ObjectT> objectClass) {

		ObjectT object = null;
		try {
			final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			try (InputStream inputStream =
					ResourceFileUtils.resourceFileToInputStream(yamlResourceFilePathString)) {

				if (inputStream == null) {
					Logger.printError("YAML resource file not found:" +
							System.lineSeparator() + yamlResourceFilePathString);

				} else {
					object = mapper.readValue(inputStream, objectClass);
				}
			}

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
		}
		return object;
	}
}
