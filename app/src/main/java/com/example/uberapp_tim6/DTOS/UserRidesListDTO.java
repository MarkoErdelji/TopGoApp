package com.example.uberapp_tim6.DTOS;

import java.util.List;

public class UserRidesListDTO {
    private int totalCount;

    private List<RideDTO> results;

    public UserRidesListDTO() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<RideDTO> getResults() {
        return results;
    }

    public void setResults(List<RideDTO> results) {
        this.results = results;
    }
}
