package io.digitalreactor.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ingvard on 11.04.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexResponse<T> {

    @JsonProperty("total_rows")
    private Long totalRows;

    @JsonProperty("sampled")
    private boolean sampled;

    @JsonProperty("sample_share")
    private double sampleShare;

    @JsonProperty("sample_size")
    private long sampleSize;

    @JsonProperty("sample_space")
    private long sampleSpace;

    @JsonProperty("data_lag")
    private int dataLag;

    @JsonProperty("query")
    private Query query;

    @JsonProperty("totals")
    private List<Double> totals;

    @JsonProperty("min")
    private List<Double> min;

    @JsonProperty("max")
    private List<Double> max;

    @JsonProperty("data")
    private T data;

}
