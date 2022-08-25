package com.utils.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public final class ScriptUtils {

	private ScriptUtils() {
	}

	public static ScriptEngine createScriptEngine() {

		System.setProperty("nashorn.args", "--no-deprecation-warning");
		final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		return scriptEngineManager.getEngineByName("JavaScript");
	}
}
