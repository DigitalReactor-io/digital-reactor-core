package io.digitalreactor.core.domain;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public class DigitalReactOptions {
    private String token;

    private DigitalReactOptions(Builder builder) {
        this.token = builder.token;
    }

    public String getToken() {
        return token;
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        private String token;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public DigitalReactOptions build() {
            return new DigitalReactOptions(this);
        }
    }
}
