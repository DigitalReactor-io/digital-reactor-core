package io.digitalreactor.core.api.yandex.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public class RequestCounters extends AbstractRequest {
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
    private String token;
    private String prefix;

    private RequestCounters() {
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

    public String getToken() {
        return token;
    }

    @Override
    public String toQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(prefix);
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
        if (StringUtils.isNoneEmpty(token)) {
            builder.append("&oauth_token=").append(token);
        }

        return builder.toString();
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        private RequestCounters requestCounters = new RequestCounters();

        public Builder prefix(String prefix) {
            requestCounters.prefix = prefix;
            return this;
        }

        public Builder token(String token) {
            requestCounters.token = token;
            return this;
        }

        public Builder favorite(Boolean favorite) {
            requestCounters.favorite = favorite;
            return this;
        }

        public Builder field(String field) {
            requestCounters.field = field;
            return this;
        }

        public Builder labelId(Integer labelId) {
            requestCounters.labelId = labelId;
            return this;
        }

        public Builder offset(Integer offset) {
            requestCounters.offset = offset;
            return this;
        }

        public Builder perPage(Integer perPage) {
            requestCounters.perPage = perPage;
            return this;
        }

        public Builder permission(String permission) {
            requestCounters.permission = permission;
            return this;
        }

        public Builder reverse(Boolean reverse) {
            requestCounters.reverse = reverse;
            return this;
        }

        public Builder searchString(String searchString) {
            requestCounters.searchString = searchString;
            return this;
        }

        public Builder status(Status status) {
            requestCounters.status = status;
            return this;
        }

        public Builder type(Type type) {
            requestCounters.type = type;
            return this;
        }

        public RequestCounters build() {
            return requestCounters;
        }
    }

}
