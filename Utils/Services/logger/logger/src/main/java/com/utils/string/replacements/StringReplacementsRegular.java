package com.utils.string.replacements;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.string.StrUtils;

public class StringReplacementsRegular implements StringReplacements {

	private final List<ReplacementData> replacementDataList;

	public StringReplacementsRegular() {

		replacementDataList = new ArrayList<>();
	}

	@Override
	public void addReplacement(
			final String searchString,
			final String replacementString) {

		final ReplacementData replacementData =
				new ReplacementData(searchString, replacementString);
		replacementDataList.add(replacementData);
	}

	@Override
	public String performReplacements(
			final String str) {

		String resultStr = str;
		for (final ReplacementData replacementData : replacementDataList) {

			final String searchString = replacementData.searchString;
			final String replacementString = replacementData.replacementString;
			resultStr = StringUtils.replace(resultStr, searchString, replacementString);
		}
		return resultStr;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	private static class ReplacementData {

		final String searchString;
		final String replacementString;

		ReplacementData(
				final String searchString,
				final String replacementString) {

			this.searchString = searchString;
			this.replacementString = replacementString;
		}

		@Override
		public String toString() {
			return StrUtils.reflectionToString(this);
		}
	}
}
