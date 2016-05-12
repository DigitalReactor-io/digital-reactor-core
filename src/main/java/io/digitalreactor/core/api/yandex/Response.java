package io.digitalreactor.core.api.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public class Response {
    @JsonProperty("total_rows")
    private Long totalRows;

    private Boolean sampled;

    @JsonProperty("sample_share")
    private Double sampleShare;

    @JsonProperty("sample_size")
    private Long sampleSize;

    @JsonProperty("sample_space")
    private Long sampleSpace;

    @JsonProperty("data_lag")
    private Integer dataLag;

    @JsonProperty("query")
    private RequestTable requestTable;

    private List<Double> totals;

    private List<Double> min;

    private List<Double> max;

    @JsonProperty("data")
    private List<Data> datas;

    public Long getTotalRows() {
        return totalRows;
    }

    public Boolean getSampled() {
        return sampled;
    }

    public Double getSampleShare() {
        return sampleShare;
    }

    public Long getSampleSize() {
        return sampleSize;
    }

    public Long getSampleSpace() {
        return sampleSpace;
    }

    public Integer getDataLag() {
        return dataLag;
    }

    public RequestTable getRequestTable() {
        return requestTable;
    }

    public List<Double> getTotals() {
        return totals;
    }

    public List<Double> getMin() {
        return min;
    }

    public List<Double> getMax() {
        return max;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public static class Data {
        private List<String> dimensions;
        private List<Double> metrics;

        public List<String> getDimensions() {
            return dimensions;
        }

        public List<Double> getMetrics() {
            return metrics;
        }
    }
}
