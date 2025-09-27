package com.personal.utils.app_info;

import com.utils.app_info.AppInfo;
import com.utils.app_info.FactoryAppInfo;

public final class FactoryAppInfoUtilsManager {

	private FactoryAppInfoUtilsManager() {
	}

	public static AppInfo computeInstance() {

		final String appTitleDefault = "UtilsManager";
		final String appVersionDefault = "0.0.3";
		return FactoryAppInfo.computeInstance(appTitleDefault, appVersionDefault);
	}
}
