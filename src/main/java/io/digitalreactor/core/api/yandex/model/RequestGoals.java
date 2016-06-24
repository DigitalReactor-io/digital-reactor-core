package io.digitalreactor.core.api.yandex.model;

/**
 * Created by MStepachev on 22.06.2016.
 */
public class RequestGoals extends AbstractRequest {

    private Long counterId;
    private String token;
    private String prefix;

    @Override
    public String toQuery() {
        final StringBuilder builder = new StringBuilder();
        if (prefix == null) {
            throw new IllegalArgumentException("invalid prefix for goals request");
        } else {
            builder.append(prefix);
        }
        if (counterId == null) {
            throw new IllegalArgumentException("counterId must be defined");
        } else {
            builder.append(counterId);
        }
        if (token == null) {
            throw new IllegalArgumentException("token must be defined");
        } else {
            builder.append("/goals?oauth_token=");
            builder.append(token);
        }

        return builder.toString();
    }

    public static RequestGoals.Builder of() {
        return new RequestGoals.Builder();
    }

    public static class Builder {
        private RequestGoals requestGoals = new RequestGoals();

        public Builder prefix(String prefix) {
            requestGoals.prefix = prefix;

            return this;
        }

        public Builder token(String token) {
            requestGoals.token = token;

            return this;
        }

        public Builder counterId(Long counterId) {
            requestGoals.counterId = counterId;

            return this;
        }

        public RequestGoals build() {
            return requestGoals;
        }
    }
}
