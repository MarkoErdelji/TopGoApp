package com.example.uberapp_tim6.DTOS;

import java.util.ArrayList;
import java.util.List;

public class DriverWorkHoursDTO {

    public PaginatedResponse totalCount;
    public List<WorkHoursDTO> results = new ArrayList<WorkHoursDTO>();

    public DriverWorkHoursDTO() {
    }


    public PaginatedResponse getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public List<WorkHoursDTO> getResults() {
        return results;
    }

    public void setResults(List<WorkHoursDTO> results) {
        this.results = results;
    }
}
