package com.utils.srec;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.srec.record_types.FactorySRecRecordType;
import com.utils.srec.record_types.SRecRecordType;
import com.utils.srec.records.SRecRecord;

public final class FactorySRecFile {

	private FactorySRecFile() {
	}

	public static SRecFile newInstance(
			final String sRecFilePathString) {

		SRecFile sRecFile = null;
		try {
			Logger.printProgress("parsing SREC file:");
			Logger.printLine(sRecFilePathString);

			final Path sRecFilePath = Paths.get(sRecFilePathString);
			if (!IoUtils.fileExists(sRecFilePath)) {
				Logger.printWarning("the SREC file does not exist!");

			} else {
				final List<SRecRecord> sRecRecordList = new ArrayList<>();
				try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(sRecFilePath))) {

					int state = 0;
					SRecRecordType sRecRecordType = null;
					int byteCount = 0;
					long address = 0;
					while (true) {

						if (state == 0) {

							final int b = inputStream.read();
							if (b == -1) {
								state = -1;
							} else {
								if (b == 'S') {
									state = 1;
								} else if (b != '\n' && b != '\r') {
									Logger.printWarning("found unexpected character " + b +
											" in the SREC file (expected S)!");
								}
							}

						} else if (state == 1) {

							final int recordTypeCode = parseHexDigit(inputStream);
							if (recordTypeCode == -1) {
								state = -1;
							} else {
								sRecRecordType = FactorySRecRecordType.INSTANCE.computeInstance(recordTypeCode);
								if (sRecRecordType == null) {

									Logger.printWarning("invalid record type " + recordTypeCode +
											" in the SREC file!");
									state = 0;

								} else {
									state = 2;
								}
							}

						} else if (state == 2) {

							final int number = parseSRecNumber(inputStream);
							if (number == -1) {
								state = -1;
							} else {
								byteCount = number - sRecRecordType.getAddressByteCount() - 1;
								state = 3;
							}

						} else if (state == 3) {

							final int addressByteCount = sRecRecordType.getAddressByteCount();
							address = parseSRecLongNumber(inputStream, addressByteCount * 2);
							if (address == -1) {
								state = -1;
							} else {
								state = 4;
							}

						} else if (state == 4) {

							final byte[] data = new byte[byteCount];
							for (int i = 0; i < byteCount; i++) {

								final int dataByte = parseSRecNumber(inputStream);
								if (dataByte == -1) {

									state = -1;
									break;
								}
								data[i] = (byte) dataByte;
							}
							if (state != -1) {

								final SRecRecord sRecRecord =
										new SRecRecord(sRecRecordType, address, data);
								sRecRecordList.add(sRecRecord);
								state = 5;
							}

						} else if (state == 5) {

							final int checksum = parseSRecNumber(inputStream);
							if (checksum == -1) {
								state = -1;
							} else {
								state = 0;
							}

						} else {
							break;
						}
					}
				}
				sRecFile = new SRecFile(sRecRecordList);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to parse SREC file:" +
					System.lineSeparator() + sRecFilePathString);
			Logger.printException(exc);
		}
		return sRecFile;
	}

	private static int parseSRecNumber(
			final InputStream inputStream) throws Exception {

		int sRecNumber = 0;
		final int numberLength = 2;
		for (int i = 0; i < numberLength; i++) {

			final int hexDigit = parseHexDigit(inputStream);
			if (hexDigit == -1) {
				sRecNumber = -1;
				break;
			}

			sRecNumber += hexDigit << ((numberLength - 1 - i) * 4);
		}
		return sRecNumber;
	}

	private static long parseSRecLongNumber(
			final InputStream inputStream,
			final int numberLength) throws Exception {

		long sRecNumber = 0;
		for (int i = 0; i < numberLength; i++) {

			final long hexDigit = parseHexDigit(inputStream);
			if (hexDigit == -1) {
				sRecNumber = -1;
				break;
			}

			sRecNumber += hexDigit << ((numberLength - 1 - i) * 4);
		}
		return sRecNumber;
	}

	private static int parseHexDigit(
			final InputStream inputStream) throws Exception {

		final int hexDigitValue;
		final int b = inputStream.read();
		if (b == -1) {
			Logger.printWarning("unexpected end of stream while parsing the SREC file!");
			hexDigitValue = -1;
		} else if ('0' <= b && b <= '9') {
			hexDigitValue = b - '0';
		} else if ('A' <= b && b <= 'F') {
			hexDigitValue = b - 'A' + 10;
		} else if ('a' <= b && b <= 'f') {
			hexDigitValue = b - 'a' + 10;
		} else {
			Logger.printWarning("invalid hex digit value " + b + " in the SREC file!");
			hexDigitValue = -1;
		}
		return hexDigitValue;
	}
}
