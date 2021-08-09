package com.vodafone.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.apalya.exceptions.NoDataPresentException;
import com.apalya.models.Channel;

public class CSVParserUtil {

	private static List<CSVRecord> parseRecords(String fileName) {
		Reader in = null;
		try {
			in = new FileReader(System.getProperty("user.dir") + "/src/main/resources/testdata/"+fileName);
			return CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in).getRecords();
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	private static Optional<CSVRecord> getFirstRecord(String csvName, String testCaseName) {
		List<CSVRecord> records = parseRecords(csvName);
		return records.parallelStream().filter(record -> record.get("TestName").equals(testCaseName)).findFirst();
	}

	public static Channel getChannels(String tcName) {
		try
		{
			Optional<CSVRecord> csvRecord = getFirstRecord("testdata.csv", tcName);
			if (csvRecord.isPresent()) {
				CSVRecord record = csvRecord.get();
				return Channel.builder()
						.ChannelName(record.get("ChannelName"))
						.build();
			}
		}
		catch(Exception e)
		{
			throw new NoDataPresentException();
		}
		return null;
		
		
	}
}
