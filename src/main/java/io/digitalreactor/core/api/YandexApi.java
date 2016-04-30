package io.digitalreactor.core.api;

import java.util.function.Consumer;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public interface YandexApi {

    String tables(RequestTable requestTable);

    void tables(RequestTable requestTable, Consumer<String> consumer);

    String counters(RequestCounterList requestCounterList);

    void counters(RequestCounterList requestCounterList, Consumer<String> consumer);
}
