package io.digitalreactor.core.api.yandex;

import io.digitalreactor.core.api.yandex.counter.Status;
import io.digitalreactor.core.api.yandex.counter.Type;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by flaidzeres on 30.04.16.
 */
public class RequestCounterList implements Request {
    private Boolean favorite;
    private String field;
    private Integer labelId;
    private Integer offset;
    private Integer perPage;
    private String permission;
    private Boolean reverse;
    private String searchString;
    private Status status;
    private Type type;

    private String PREFIX = "/management/v1/counters?";

    public RequestCounterList(Builder builder) {
        this.favorite = builder.favorite;
        this.field = builder.field;
        this.labelId = builder.labelId;
        this.offset = builder.offset;
        this.perPage = builder.perPage;
        this.permission = builder.permission;
        this.reverse = builder.reverse;
        this.searchString = builder.searchString;
        this.status = builder.status;
        this.type = builder.type;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public String getField() {
        return field;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public String getPermission() {
        return permission;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public String getSearchString() {
        return searchString;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toQuery() {
        final StringBuilder builder = new StringBuilder();
        if (favorite != null) {
            int f = 0;
            if (favorite) {
                f = 1;
            }
            builder.append("&favorite=").append(f);
        }
        if (StringUtils.isNotEmpty(field)) {
            builder.append("&field=").append(field);
        }
        if (labelId != null) {
            builder.append("&label_id=").append(labelId);
        }
        if (offset != null) {
            builder.append("&offset=").append(offset);
        }
        if (perPage != null) {
            builder.append("&per_page=").append(perPage);
        }
        if (StringUtils.isNotEmpty(permission)) {
            builder.append("&permission=").append(permission);
        }
        if (reverse != null) {
            builder.append("&reverse=").append(reverse);
        }
        if (StringUtils.isNotEmpty(searchString)) {
            builder.append("&search_string=").append(searchString);
        }
        if (StringUtils.isNotEmpty(searchString)) {
            builder.append("&search_string=").append(searchString);
        }

        // TODO: 30.04.16  complete all field

        return builder.toString();
    }

    @Override
    public String prefix() {
        return PREFIX;
    }

    public static RequestCounterList.Builder of() {
        return new RequestCounterList.Builder();
    }

    public static class Builder {
        private Boolean favorite;
        private String field;
        private Integer labelId;
        private Integer offset;
        private Integer perPage;
        private String permission;
        private Boolean reverse;
        private String searchString;
        private Status status;
        private Type type;

        public Builder favorite(Boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder labelId(Integer labelId) {
            this.labelId = labelId;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }

        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder reverse(Boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public Builder searchString(String searchString) {
            this.searchString = searchString;
            return this;
        }


        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public RequestCounterList build() {
            return new RequestCounterList(this);
        }
    }

}
