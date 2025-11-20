package com.employee.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaginationResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;

}
