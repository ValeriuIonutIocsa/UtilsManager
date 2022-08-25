package com.utils.srec.symbols;

import com.utils.string.StrUtils;

public class SRecPatcherSymbol {

	private final long startAddress;
	private final byte[] symbolContent;

	public SRecPatcherSymbol(
			final long startAddress,
			final byte[] symbolContent) {

		this.startAddress = startAddress;
		this.symbolContent = symbolContent;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public long getStartAddress() {
		return startAddress;
	}

	public byte[] getSymbolContent() {
		return symbolContent;
	}
}
