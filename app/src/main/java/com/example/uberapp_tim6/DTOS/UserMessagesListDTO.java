package com.example.uberapp_tim6.DTOS;

import java.util.ArrayList;
import java.util.List;

public class UserMessagesListDTO {


    private int totalCount;

    private List<UserMessagesDTO> results;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserMessagesDTO> getResults() {
        return results;
    }

    public void setResults(List<UserMessagesDTO> results) {
        this.results = results;
    }

    public UserMessagesListDTO() {
        results = new ArrayList<>();
    }
}
