package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.tools.PaginatedResponse;

import java.util.ArrayList;
import java.util.List;

public class DriverReviewListDTO {
    private int totalCount;
    private List<CreateReviewResponseDTO> results;

    public DriverReviewListDTO(){}

    public DriverReviewListDTO(int totalCount, List<CreateReviewResponseDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CreateReviewResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<CreateReviewResponseDTO> results) {
        this.results = results;
    }
}
