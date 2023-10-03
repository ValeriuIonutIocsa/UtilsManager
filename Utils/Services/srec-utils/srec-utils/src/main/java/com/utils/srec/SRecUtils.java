package com.utils.srec;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.csv.AbstractCsvWriter;
import com.utils.io.ReaderUtils;
import com.utils.log.Logger;
import com.utils.srec.record_types.SRecRecordType;
import com.utils.srec.records.SRecRecord;
import com.utils.srec.symbols.SRecPatcherSymbol;
import com.utils.string.StrUtils;

public final class SRecUtils {

	private SRecUtils() {
	}

	public static String writePatchDataCsv(
			final String csvName,
			final List<SRecPatcherSymbol> sRecPatcherSymbolList,
			final String patchDataCsvPathString) {

		new AbstractCsvWriter(csvName, patchDataCsvPathString) {

			@Override
			protected void write(
					final PrintStream printStream) {

				for (final SRecPatcherSymbol sRecPatcherSymbol : sRecPatcherSymbolList) {

					final long startAddress = sRecPatcherSymbol.getStartAddress();
					final byte[] symbolContent = sRecPatcherSymbol.getSymbolContent();
					printStream.print(StrUtils.createHexString(startAddress));
					printStream.print(',');
					printStream.print(StrUtils.byteArrayToHexString(symbolContent));
					printStream.println();
				}
			}

		}.writeCsv();
		return patchDataCsvPathString;
	}

	public static void comparePatchedWithOriginal(
			final String patchDataCsvPathString,
			final String outputSRecFilePathString,
			final String sRecFilePathString) {

		Logger.printProgress("comparing patched SREC file:" + System.lineSeparator() +
				outputSRecFilePathString + System.lineSeparator() +
				"with original SREC file: " + sRecFilePathString);

		final Map<Long, Byte> patchedByteByAddressMap = new HashMap<>();
		fillPatchedByteByAddressMap(patchDataCsvPathString, patchedByteByAddressMap);

		final SRecFile outputSRecFile = FactorySRecFile.newInstance(outputSRecFilePathString, "patched SREC");
		final SRecFile sRecFile = FactorySRecFile.newInstance(sRecFilePathString, "original SREC");

		final List<SRecRecord> outputSRecRecordList = outputSRecFile.getSRecRecordList();
		final List<SRecRecord> sRecRecordList = sRecFile.getSRecRecordList();
		if (outputSRecRecordList.size() != sRecRecordList.size()) {
			Logger.printError("patched SREC record count " + outputSRecRecordList.size() +
					" different than original SREC record count " + sRecRecordList.size());

		} else {
			for (int i = 0; i < sRecRecordList.size(); i++) {

				final SRecRecord outputSRecRecord = outputSRecRecordList.get(i);
				final SRecRecord sRecRecord = sRecRecordList.get(i);
				compareSRecRecords(outputSRecRecord, sRecRecord, patchedByteByAddressMap);
			}
		}
	}

	private static void fillPatchedByteByAddressMap(
			final String patchDataCsvPathString,
			final Map<Long, Byte> patchedByteByAddressMap) {

		try {
			Logger.printProgress("parsing patch data CSV file:");
			Logger.printLine(patchDataCsvPathString);

			try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(patchDataCsvPathString)) {

				String line;
				while ((line = bufferedReader.readLine()) != null) {

					final String[] lineSplitPartArray = StringUtils.split(line, ',');
					if (lineSplitPartArray.length == 2) {

						final String addressString = lineSplitPartArray[0];
						final long address = StrUtils.tryParsePositiveLongFromHexString(addressString);
						if (address >= 0) {

							final String byteArrayString = lineSplitPartArray[1];
							final byte[] byteArray =
									StrUtils.tryParseByteArrayFromHexString(byteArrayString);
							if (byteArray != null) {

								for (int i = 0; i < byteArray.length; i++) {

									final byte b = byteArray[i];
									patchedByteByAddressMap.put(address + i, b);
								}
							}
						}
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to patch the patch data CSV file:" +
					System.lineSeparator() + patchDataCsvPathString);
			Logger.printException(exc);
		}
	}

	private static void compareSRecRecords(
			final SRecRecord outputSRecRecord,
			final SRecRecord sRecRecord,
			final Map<Long, Byte> patchedByteByAddressMap) {

		final SRecRecordType outputSRecRecordType = outputSRecRecord.getSRecRecordType();
		final SRecRecordType sRecRecordType = sRecRecord.getSRecRecordType();
		if (outputSRecRecordType != sRecRecordType) {
			Logger.printError("patched SREC record type " + outputSRecRecordType +
					" different than original SREC record type " + sRecRecordType);

		} else {
			final long outputStartAddress = outputSRecRecord.getStartAddress();
			final long startAddress = sRecRecord.getStartAddress();
			if (outputStartAddress != startAddress) {
				Logger.printError("patched SREC record start address " + StrUtils.createHexString(outputStartAddress) +
						" different than original SREC record start address " + StrUtils.createHexString(startAddress));

			} else {
				final byte[] outputData = outputSRecRecord.getData();
				final byte[] data = sRecRecord.getData();
				if (outputData.length != data.length) {
					Logger.printError("patched SREC record data length " + outputData.length +
							" different than original SREC record data length " + data.length);

				} else {
					compareSRecRecordData(outputData, data, outputStartAddress, patchedByteByAddressMap);
				}
			}
		}
	}

	private static void compareSRecRecordData(
			final byte[] outputData,
			final byte[] data,
			final long outputStartAddress,
			final Map<Long, Byte> patchedByteByAddressMap) {

		for (int i = 0; i < data.length; i++) {

			final byte outputDataByte = outputData[i];

			final long outputDataByteAddress = outputStartAddress + i;
			final Byte patchedOutputDataByte = patchedByteByAddressMap.get(outputDataByteAddress);
			if (patchedOutputDataByte != null) {

				if (outputDataByte != patchedOutputDataByte) {

					Logger.printError("patched SREC record data byte " +
							StrUtils.createHexString(Byte.toUnsignedLong(outputDataByte)) +
							" different than expected value " +
							StrUtils.createHexString(Byte.toUnsignedLong(patchedOutputDataByte)) +
							" at address " + StrUtils.createHexString(outputDataByteAddress));
				}

			} else {
				final byte dataByte = data[i];
				if (outputDataByte != dataByte) {

					Logger.printError("patched SREC record data byte " +
							StrUtils.createHexString(Byte.toUnsignedLong(outputDataByte)) +
							" different than original SREC record data length " +
							StrUtils.createHexString(Byte.toUnsignedLong(dataByte)) +
							" at address " + StrUtils.createHexString(outputDataByteAddress));
				}
			}
		}
	}
}
