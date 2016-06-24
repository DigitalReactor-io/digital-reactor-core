package io.digitalreactor.core.api.yandex.model;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public abstract class AbstractRequest {

    public static final String DATA_BY_TYPE = "/stat/v1/data/bytime?";

    public static final String DATA = "/stat/v1/data?";

    public static final String COUNTETS = "/management/v1/counters?";

    public static final String GOALS = "/management/v1/counter/";

    public abstract String toQuery();
}
