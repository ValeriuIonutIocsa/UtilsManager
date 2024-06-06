package com.utils.srec;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.srec.record_types.SRecRecordType;
import com.utils.srec.records.SRecRecord;
import com.utils.srec.symbols.SRecParserSymbol;
import com.utils.srec.symbols.SRecPatcherSymbol;
import com.utils.string.StrUtils;

public class SRecFile {

	private final List<SRecRecord> sRecRecordList;

	SRecFile(
			final List<SRecRecord> sRecRecordList) {

		this.sRecRecordList = sRecRecordList;
	}

	public long computeStartAddress() {

		long startAddress = -1;
		for (final SRecRecord sRecRecord : sRecRecordList) {

			final SRecRecordType sRecRecordType = sRecRecord.getSRecRecordType();
			if (sRecRecordType == SRecRecordType.S3) {

				startAddress = sRecRecord.getStartAddress();
				break;
			}
		}
		return startAddress;
	}

	public long computeEndAddress() {

		long endAddress = -1;
		for (int i = sRecRecordList.size() - 1; i >= 0; i--) {

			final SRecRecord sRecRecord = sRecRecordList.get(i);
			final SRecRecordType sRecRecordType = sRecRecord.getSRecRecordType();
			if (sRecRecordType == SRecRecordType.S3) {

				endAddress = sRecRecord.getEndAddress();
				break;
			}
		}
		return endAddress;
	}

	public void parseSymbolContent(
			final Collection<SRecParserSymbol> sRecParserSymbolCollection) {

		final List<SRecRecord> sortedSRecRecordList = new ArrayList<>(sRecRecordList);
		sortedSRecRecordList.sort(Comparator.comparing(SRecRecord::getStartAddress));

		final List<SRecParserSymbol> sortedSRecParserSymbolList = new ArrayList<>(sRecParserSymbolCollection);
		sortedSRecParserSymbolList.sort(Comparator.comparing(SRecParserSymbol::getStartAddress));

		int recordIndex = 0;
		for (final SRecParserSymbol sRecParserSymbol : sortedSRecParserSymbolList) {

			final long startAddress = sRecParserSymbol.getStartAddress();
			while (true) {

				if (recordIndex < sortedSRecRecordList.size()) {

					final SRecRecord sRecRecord = sortedSRecRecordList.get(recordIndex);
					final long recordStartAddress = sRecRecord.getStartAddress();
					if (recordStartAddress <= startAddress) {

						final long recordEndAddress = sRecRecord.getEndAddress();
						if (startAddress >= recordEndAddress) {
							recordIndex++;

						} else {
							parseSymbolContent(sRecParserSymbol, sortedSRecRecordList, recordIndex);
							break;
						}
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
	}

	private static void parseSymbolContent(
			final SRecParserSymbol sRecParserSymbol,
			final List<SRecRecord> sRecRecordList,
			final int recordIndex) {

		int readingRecordIndex = recordIndex;
		long readingStartAddress = sRecParserSymbol.getStartAddress();
		int readByteCount = 0;
		while (true) {

			if (readingRecordIndex >= sRecRecordList.size()) {
				break;
			}

			final SRecRecord readingSRecRecord = sRecRecordList.get(readingRecordIndex);
			final int readingOffset = (int) (readingStartAddress - readingSRecRecord.getStartAddress());
			if (readingOffset < 0) {
				break;
			}

			final byte[] data = readingSRecRecord.getData();
			if (readingOffset == data.length) {

				readingRecordIndex++;
				continue;
			}

			final byte[] symbolContent = sRecParserSymbol.getSymbolContent();
			symbolContent[readByteCount] = data[readingOffset];
			readByteCount++;
			if (readByteCount == symbolContent.length) {

				sRecParserSymbol.setParsedSuccessfully(true);
				break;
			}

			readingStartAddress++;
		}
	}

	public void patch(
			final List<SRecPatcherSymbol> sRecPatcherSymbolList) {

		final List<SRecRecord> sortedSRecRecordList = new ArrayList<>(sRecRecordList);
		sortedSRecRecordList.sort(Comparator.comparing(SRecRecord::getStartAddress));

		final List<SRecPatcherSymbol> sortedSRecPatcherSymbolList = new ArrayList<>(sRecPatcherSymbolList);
		sortedSRecPatcherSymbolList.sort(Comparator.comparing(SRecPatcherSymbol::startAddress));

		int recordIndex = 0;
		for (final SRecPatcherSymbol sRecPatcherSymbol : sortedSRecPatcherSymbolList) {

			final long startAddress = sRecPatcherSymbol.startAddress();
			while (true) {

				if (recordIndex < sortedSRecRecordList.size()) {

					final SRecRecord sRecRecord = sortedSRecRecordList.get(recordIndex);
					final long recordStartAddress = sRecRecord.getStartAddress();
					if (recordStartAddress <= startAddress) {

						final long recordEndAddress = sRecRecord.getEndAddress();
						if (startAddress >= recordEndAddress) {
							recordIndex++;

						} else {
							patch(sRecPatcherSymbol, sortedSRecRecordList, recordIndex);
							break;
						}
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
	}

	private static void patch(
			final SRecPatcherSymbol sRecPatcherSymbol,
			final List<SRecRecord> sortedSRecRecordList,
			final int recordIndex) {

		int readingRecordIndex = recordIndex;
		long readingStartAddress = sRecPatcherSymbol.startAddress();
		int readByteCount = 0;
		while (true) {

			if (readingRecordIndex >= sortedSRecRecordList.size()) {
				break;
			}

			final SRecRecord readingSRecRecord = sortedSRecRecordList.get(readingRecordIndex);
			final int readingOffset = (int) (readingStartAddress - readingSRecRecord.getStartAddress());
			if (readingOffset < 0) {
				break;
			}

			final byte[] data = readingSRecRecord.getData();
			if (readingOffset == data.length) {

				readingRecordIndex++;
				continue;
			}

			final byte[] symbolContent = sRecPatcherSymbol.symbolContent();
			if (readByteCount == symbolContent.length) {
				break;
			}

			data[readingOffset] = symbolContent[readByteCount];

			readingStartAddress++;
			readByteCount++;
		}
	}

	public void save(
			final String copySRecFilePathString) {

		try {
			Logger.printProgress("saving SREC file to:");
			Logger.printLine(copySRecFilePathString);

			FactoryFolderCreator.getInstance()
					.createParentDirectories(copySRecFilePathString, false, true);
			FactoryReadOnlyFlagClearer.getInstance()
					.clearReadOnlyFlagFile(copySRecFilePathString, false, true);
			try (PrintStream printStream =
					StreamUtils.openPrintStream(copySRecFilePathString, false, StandardCharsets.UTF_8)) {

				for (final SRecRecord sRecRecord : sRecRecordList) {
					sRecRecord.write(printStream);
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to save SREC file");
			Logger.printException(exc);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	List<SRecRecord> getSRecRecordList() {
		return sRecRecordList;
	}
}
