package com.gerimedica.processor.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CSVServiceTest {

    private static final String CSV_NAME = "test.csv";
    private static final String CONTENT_TYPE = "text/csv";
    private static final String EXERCISE_CSV = "exercise.csv";

    @Autowired
    private CSVService csvService;

    @BeforeEach
    public void setUp() throws Exception {
        // for upload script should be used
        MultipartFile multipartFile = new MockMultipartFile(CSV_NAME,CSV_NAME, CONTENT_TYPE,
                new FileInputStream(getClass().getClassLoader().getResource(EXERCISE_CSV).getFile()));
        csvService.upload(multipartFile);
    }

    @AfterEach
    public void clear() {
        // for clear script should be used
        csvService.deleteAll();
    }

    @Test
    void fetchAll() {
        // given
        var result = csvService.fetch();

        // then
        assertEquals(18, result.size());
    }

    @Test
    void fetchById() {
        // given
        var result = csvService.findById(1);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("271636001", result.get().getCode());
    }

    @Test
    void deleteAll() {
        // given
        var current = csvService.fetch();
        assertEquals(18, current.size());

        // when
        csvService.deleteAll();

        // then
        var result = csvService.fetch();
        assertEquals(0, result.size());
    }
}