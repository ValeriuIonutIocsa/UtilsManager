package com.utils.srec.record_types;

public enum SRecRecordType {

	S0(0, 2),
	S1(1, 2),
	S2(2, 3),
	S3(3, 4),
	S5(5, 2),
	S6(6, 3),
	S7(7, 4),
	S8(8, 3),
	S9(9, 2);

	private final int code;
	private final int addressByteCount;

	SRecRecordType(
			final int code,
			final int addressByteCount) {

		this.code = code;
		this.addressByteCount = addressByteCount;
	}

	public int getCode() {
		return code;
	}

	public int getAddressByteCount() {
		return addressByteCount;
	}
}
