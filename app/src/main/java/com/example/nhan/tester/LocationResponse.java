package com.example.nhan.tester;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by nhan on 3/14/15.
 */
public class LocationResponse {
    private LocationData[] results;
    private String status;

    @JsonCreator
    public LocationResponse(@JsonProperty("results") LocationData[] results, @JsonProperty("status") String status) {
        this.results = results;
        this.status = status;
    }
    public LocationData[] getResults() {
        return results;
    }

    public void setResults(LocationData[] results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
