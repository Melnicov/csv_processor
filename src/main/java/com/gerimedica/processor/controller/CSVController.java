package com.gerimedica.processor.controller;

import com.gerimedica.processor.model.CSVResponse;
import com.gerimedica.processor.model.CSVRow;
import com.gerimedica.processor.service.CSVService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/csv")
@RequiredArgsConstructor
public class CSVController {
    private final CSVService csvService;

    @Operation(summary = "Upload CSV File", tags = "CSV Controller")
    @PostMapping
    public ResponseEntity<CSVResponse<List<CSVRow>>> upload(@RequestParam("csv") MultipartFile csv) {
        var result = csvService.upload(csv);
        log.info("Created rows : {}", result.size());
        return ResponseEntity.ok(CSVResponse.<List<CSVRow>>builder()
                                            .response(result)
                                            .build());
    }

    @Operation(summary = "Fetch All CSV Files", tags = "CSV Controller")
    @GetMapping
    public ResponseEntity<CSVResponse<List<CSVRow>>> fetch() {
        var result = csvService.fetch();
        log.info("Fetched rows : {}", result.size());
        return ResponseEntity.ok(CSVResponse.<List<CSVRow>>builder()
                                            .response(result)
                                            .build());
    }

    @Operation(summary = "Find CSV File by Id", tags = "CSV Controller")
    @GetMapping("/{id}")
    public ResponseEntity<CSVResponse<CSVRow>> findById(@PathVariable Integer id) {
        var result = csvService.findById(id);
        log.info("Fetched row : {}", result);
        return result.map(csvRow -> ResponseEntity.ok(
                CSVResponse.<CSVRow>builder()
                           .response(csvRow)
                           .build()))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete All CSV Files", tags = "CSV Controller")
    @DeleteMapping
    public ResponseEntity<CSVResponse<String>> deleteAll() {
        csvService.deleteAll();
        log.info("Deleted all rows");
        return ResponseEntity.ok(CSVResponse.<String>builder()
                                            .response("All rows were deleted.")
                                            .build());
    }
}
