package com.utils.srec.records;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import com.utils.srec.record_types.SRecRecordType;
import com.utils.string.StrUtils;

public class SRecRecord {

	private final SRecRecordType sRecRecordType;
	private final long address;
	private final byte[] data;

	public SRecRecord(
			final SRecRecordType sRecRecordType,
			final long address,
			final byte[] data) {

		this.sRecRecordType = sRecRecordType;
		this.address = address;
		this.data = data;
	}

	public void write(
			final PrintStream printStream) {

		printStream.print('S');

		final int code = sRecRecordType.getCode();
		printStream.print(code);

		final int addressByteCount = sRecRecordType.getAddressByteCount();
		final int byteCount = addressByteCount + data.length + 1;
		printStream.print(StrUtils.unsignedIntToPaddedHexString(byteCount, 2));

		final ByteBuffer addressByteBuffer = ByteBuffer.allocate(8);
		addressByteBuffer.putLong(address);
		addressByteBuffer.position(8 - addressByteCount);
		final byte[] addressByteArray = new byte[addressByteCount];
		addressByteBuffer.get(addressByteArray);
		printStream.print(StrUtils.byteArrayToHexString(addressByteArray).toUpperCase(Locale.US));

		printStream.print(StrUtils.byteArrayToHexString(data).toUpperCase(Locale.US));

		int checksum = byteCount;
		for (final byte addressByte : addressByteArray) {
			checksum += Byte.toUnsignedInt(addressByte);
		}
		for (final byte dataByte : data) {
			checksum += Byte.toUnsignedInt(dataByte);
		}
		checksum = checksum & 0xff;
		checksum = ~checksum & 0xff;
		printStream.print(StrUtils.unsignedIntToPaddedHexString(checksum, 2).toUpperCase(Locale.US));

		printStream.print(System.lineSeparator());
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public SRecRecordType getSRecRecordType() {
		return sRecRecordType;
	}

	public long getStartAddress() {
		return address;
	}

	public long getEndAddress() {
		return address + data.length;
	}

	public byte[] getData() {
		return data;
	}
}
