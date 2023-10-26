package com.gerimedica.processor.service;

import com.gerimedica.processor.model.CSVRow;
import com.gerimedica.processor.parser.CSVHelper;
import com.gerimedica.processor.repo.CSVRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CSVService {

    private final CSVHelper csvHelper;
    private final CSVRepo csvRepo;

    public List<CSVRow> upload(MultipartFile csv) {
        return csvRepo.saveAll(csvHelper.mapCSVToModel(csv));
    }

    public List<CSVRow> fetch() {
        return csvRepo.findAll();
    }

    public Optional<CSVRow> findById(Integer id) {
        return csvRepo.findById(id);
    }

    public void deleteAll() {
        csvRepo.deleteAll();
    }
}
