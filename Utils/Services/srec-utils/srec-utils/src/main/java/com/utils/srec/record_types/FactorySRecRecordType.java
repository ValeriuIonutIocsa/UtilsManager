package com.utils.srec.record_types;

public final class FactorySRecRecordType {

	public static final FactorySRecRecordType INSTANCE = new FactorySRecRecordType();

	private final SRecRecordType[] sRecRecordTypesByCodeArray;

	private FactorySRecRecordType() {

		int maxCode = Integer.MIN_VALUE;
		final SRecRecordType[] values = SRecRecordType.values();
		for (final SRecRecordType value : values) {

			final int code = value.getCode();
			maxCode = Math.max(maxCode, code);
		}

		sRecRecordTypesByCodeArray = new SRecRecordType[maxCode + 1];
		for (final SRecRecordType value : values) {

			final int code = value.getCode();
			sRecRecordTypesByCodeArray[code] = value;
		}
	}

	public SRecRecordType computeInstance(
			final int code) {

		final SRecRecordType sRecRecordType;
		if (0 <= code && code < sRecRecordTypesByCodeArray.length) {
			sRecRecordType = sRecRecordTypesByCodeArray[code];
		} else {
			sRecRecordType = null;
		}
		return sRecRecordType;
	}
}
