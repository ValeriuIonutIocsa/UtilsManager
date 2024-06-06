package com.utils.js;

import javax.script.ScriptEngine;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.utils.log.Logger;

public final class ScriptUtils {

	private ScriptUtils() {
	}

	public static ScriptEngine createScriptEngine() {

		final NashornScriptEngineFactory nashornScriptEngineFactory = new NashornScriptEngineFactory();
		final ScriptEngine scriptEngine = nashornScriptEngineFactory.getScriptEngine("-scripting");
		if (scriptEngine == null) {
			Logger.printError("script engine is unavailable");
		}
		return scriptEngine;
	}
}
