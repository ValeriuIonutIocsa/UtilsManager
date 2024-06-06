package com.utils.srec.symbols;

import com.utils.string.StrUtils;

public record SRecPatcherSymbol(
		long startAddress,
		byte[] symbolContent) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
