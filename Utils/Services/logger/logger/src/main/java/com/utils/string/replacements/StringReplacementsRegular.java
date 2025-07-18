package com.utils.string.replacements;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Strings;

import com.utils.string.StrUtils;

public class StringReplacementsRegular implements StringReplacements {

	private final List<StringReplacementsRegularData> stringReplacementsRegularDataList;

	public StringReplacementsRegular() {

		stringReplacementsRegularDataList = new ArrayList<>();
	}

	@Override
	public void addReplacement(
			final String searchString,
			final String replacementString) {

		final StringReplacementsRegularData stringReplacementsRegularData =
				new StringReplacementsRegularData(searchString, replacementString);
		stringReplacementsRegularDataList.add(stringReplacementsRegularData);
	}

	@Override
	public String performReplacements(
			final String str) {

		String resultStr = str;
		for (final StringReplacementsRegularData stringReplacementsRegularData : stringReplacementsRegularDataList) {

			final String searchString = stringReplacementsRegularData.searchString();
			final String replacementString = stringReplacementsRegularData.replacementString();
			resultStr = Strings.CS.replace(resultStr, searchString, replacementString);
		}
		return resultStr;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
