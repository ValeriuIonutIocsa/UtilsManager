package com.utils.jdbc.data_sources.supabase;

import java.io.InputStream;
import java.util.Properties;

import com.utils.io.PathUtils;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;

public final class SupabaseDbUtils {

	private SupabaseDbUtils() {
	}

	public static Properties createProperties() {

		final Properties properties = new Properties();
		final String propertiesFilePathString = PathUtils.computePath(
				PathUtils.createRootPath(), "tmp", "_cnf", "supabase_db.properties");
		Logger.printProgress("loading Supabase DB properties from:");
		Logger.printLine(propertiesFilePathString);
		try (InputStream inputStream = StreamUtils.openBufferedInputStream(propertiesFilePathString)) {
			properties.load(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to load Supabase DB properties");
			Logger.printException(exc);
		}
		return properties;
	}
}
