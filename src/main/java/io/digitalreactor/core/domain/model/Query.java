package io.digitalreactor.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ingvard on 11.04.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {

    @JsonProperty("ids")
    private List<Integer> ids;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("preset")
    private String preset;

    @JsonProperty("dimensions")
    private List<String> dimensions;

    @JsonProperty("metrics")
    private List<String> metrics;

    @JsonProperty("sort")
    private List<String> sort;

    @JsonProperty("date1")
    private String startDate;

    @JsonProperty("date2")
    private String endDate;

    @JsonProperty("filters")
    private String filters;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;
}
