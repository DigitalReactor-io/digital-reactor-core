package io.digitalreactor.core.api.yandex;

import java.util.function.Consumer;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public interface YandexApi {

    String tables(RequestTable requestTable, String token);

    void tables(RequestTable requestTable, String token, Consumer<String> consumer);

    String counters(RequestCounterList requestCounterList, String token);

    void counters(RequestCounterList requestCounterList, String token, Consumer<String> consumer);
}
