package io.digitalreactor.core.api;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public class RequestTable implements Request{
    private List<String> directClientLogins;
    private List<String> ids;
    private String date1;
    private String date2;
    private List<String> dimensions;
    private List<String> metrics;
    private String lang;
    private Integer limit;
    private Integer offset;
    private Boolean pretty;

    private String PREFIX = "/stat/v1/data?";

    private RequestTable(Builder builder) {
        this.directClientLogins = builder.directClientLogins;
        this.ids = builder.ids;
        this.metrics = builder.metrics;
        this.date1 = builder.date1;
        this.date2 = builder.date2;
        this.dimensions = builder.dimensions;
        this.lang = builder.lang;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.pretty = builder.pretty;
    }

    public List<String> getDirectClientLogins() {
        return directClientLogins;
    }

    public List<String> getIds() {
        return ids;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public String getDate1() {
        return date1;
    }

    public String getDate2() {
        return date2;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public String getLang() {
        return lang;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public Boolean getPretty() {
        return pretty;
    }

    @Override
    public String toQuery() {
        final StringBuilder builder = new StringBuilder();
        if (isNotEmpty(directClientLogins)) {
            builder.append("&direct_client_logins=").append(StringUtils.join(directClientLogins, ","));
        }
        if (isNotEmpty(ids)) {
            builder.append("&ids=").append(StringUtils.join(ids, ","));
        }
        if (StringUtils.isNotEmpty(date1)) {
            builder.append("&date1=").append(date1);
        }
        if (StringUtils.isNotEmpty(date1)) {
            builder.append("&date2=").append(date2);
        }
        if (isNotEmpty(dimensions)) {
            builder.append("&dimensions=").append(StringUtils.join(ids, ","));
        }
        if (isNotEmpty(metrics)) {
            builder.append("&metrics=").append(StringUtils.join(metrics, ","));
        }
        if (limit != null) {
            builder.append("&limit=").append(limit);
        }
        if (offset != null) {
            builder.append("&offset=").append(offset);
        }
        if (pretty != null) {
            builder.append("&pretty=").append(pretty);
        }
        return builder.toString().replaceFirst("&", "");
    }

    @Override
    public String prefix() {
        return PREFIX;
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        private List<String> directClientLogins;
        private List<String> ids;
        private String date1;
        private String date2;
        private List<String> dimensions;
        private List<String> metrics;
        private String lang;
        private Integer limit;
        private Integer offset;
        private Boolean pretty;

        public Builder directClientLogins(String... directClientLogins) {
            this.directClientLogins = Arrays.asList(directClientLogins);
            return this;
        }

        public Builder ids(String... ids) {
            this.ids = Arrays.asList(ids);
            return this;
        }

        public Builder date1(String date1) {
            this.date1 = date1;
            return this;
        }

        public Builder date2(String date2) {
            this.date2 = date2;
            return this;
        }

        public Builder dimensions(String... dimensions) {
            this.dimensions = Arrays.asList(dimensions);
            return this;
        }

        public Builder metrics(String... metrics) {
            this.metrics = Arrays.asList(metrics);
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder pretty(Boolean pretty) {
            this.pretty = pretty;
            return this;
        }

        public RequestTable build() {
            return new RequestTable(this);
        }
    }

}
