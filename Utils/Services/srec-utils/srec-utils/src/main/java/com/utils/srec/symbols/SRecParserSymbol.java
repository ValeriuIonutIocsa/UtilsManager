package com.utils.srec.symbols;

import com.utils.string.StrUtils;

public class SRecParserSymbol {

	private final long startAddress;
	private final int size;

	private final byte[] symbolContent;

	private boolean parsedSuccessfully;

	public SRecParserSymbol(
			final long startAddress,
			final int size) {

		this.startAddress = startAddress;
		this.size = size;

		symbolContent = new byte[size];
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public long getStartAddress() {
		return startAddress;
	}

	public int getSize() {
		return size;
	}

	public byte[] getSymbolContent() {
		return symbolContent;
	}

	public void setParsedSuccessfully(
			final boolean parsedSuccessfully) {
		this.parsedSuccessfully = parsedSuccessfully;
	}

	public boolean isParsedSuccessfully() {
		return parsedSuccessfully;
	}
}
