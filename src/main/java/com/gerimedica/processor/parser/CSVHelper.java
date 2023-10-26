package com.gerimedica.processor.parser;

import com.gerimedica.processor.exception.CSVException;
import com.gerimedica.processor.model.CSVRow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVHelper {
    private final static String TYPE = "text/csv";

    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final int DEFAULT_SORT_VAL = -1;

    public List<CSVRow> mapCSVToModel(MultipartFile csv) {
        validateFile(csv);
        try (var is = csv.getInputStream()) {
            var rows = parseRows(is);
            if (!validateCSVRow(rows)) {
                throw new CSVException("All rows should contain code field", HttpStatus.BAD_REQUEST);
            }
            return rows;
        } catch (IOException ex) {
            log.error("Failed to process csv", ex);
            throw new CSVException("Failed to process csv file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void validateFile(MultipartFile csv) {
        var fileFormat = csv.getContentType();
        if (!TYPE.equals(fileFormat)) {
            log.error("Failed to validate csv");
            throw new CSVException("File has different format : " + fileFormat, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean validateCSVRow(List<CSVRow> rows) {
        for (CSVRow r : rows) {
            if (r.getCode().isEmpty()) return false;
        }
        return true;
    }

    private List<CSVRow> parseRows(InputStream is) {
        try (var fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.Builder.create()
                                      .setHeader()
                                      .setSkipHeaderRecord(true)
                                      .setIgnoreHeaderCase(true)
                                      .setTrim(true)
                                      .build())){

            var csvRows = new ArrayList<CSVRow>();
            csvParser.getRecords().forEach(csvRecord -> {
                var row = CSVRow.builder()
                                     .source(csvRecord.get(0))
                                             .codeListCode(csvRecord.get(1))
                                             .code(csvRecord.get(2))
                                             .displayValue(csvRecord.get(3))
                                             .longDescription(csvRecord.get(4))
                                             .sortingPriority(csvRecord.get(7).isEmpty() ? DEFAULT_SORT_VAL : Integer.parseInt(csvRecord.get(7)));

                if (!csvRecord.get(5).isEmpty()) row.fromDate(LocalDate.parse(csvRecord.get(5), DateTimeFormatter.ofPattern(DATE_PATTERN)));
                if (!csvRecord.get(6).isEmpty()) row.fromDate(LocalDate.parse(csvRecord.get(6), DateTimeFormatter.ofPattern(DATE_PATTERN)));

                csvRows.add(row.build());
            });
            return csvRows;
        } catch (IOException e) {
            log.error("Failed to validate csv", e);
            throw new CSVException("Failed to parse CSV file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
