package com.gerimedica.processor.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CSVResponse<T> {
    private T response;
}
