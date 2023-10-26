package com.gerimedica.processor.repo;

import com.gerimedica.processor.model.CSVRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVRepo extends JpaRepository<CSVRow, Integer> {

}