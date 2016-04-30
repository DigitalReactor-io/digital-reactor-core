package io.digitalreactor.core;

import io.digitalreactor.core.api.RequestCounterList;
import io.digitalreactor.core.api.RequestTable;
import io.digitalreactor.core.api.YandexApi;
import io.digitalreactor.core.api.YandexApiImpl;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
@RunWith(VertxUnitRunner.class)
public class YandexApiTest {

    private YandexApi yandexApi = new YandexApiImpl(Vertx.vertx(), "ARRC9_4AAr_pqwmAhZwJQMWm2OAqi47ewg");

    @Test
    public void tables_async(TestContext context) {
        Async async = context.async();

        RequestTable requestTable = RequestTable.of()
                .ids("31424723", "29235010")
                .metrics("ym:s:visits")
                .pretty(true)
                .build();

        yandexApi.tables(requestTable, result -> {
            Assert.assertTrue(StringUtils.isNotEmpty(result));
            System.out.println(result);
            async.complete();
        });
    }

    @Test
    public void tables_blocking() {
        RequestTable requestTable = RequestTable.of()
                .ids("31424723", "29235010")
                .metrics("ym:s:visits")
                .pretty(true)
                .build();

        String result = yandexApi.tables(requestTable);

        Assert.assertTrue(StringUtils.isNotEmpty(result));

        System.out.println(result);
    }

    @Test
    public void get_counters_list_async(TestContext context) {
        Async async = context.async();

        yandexApi.counters(RequestCounterList.of().build(), result -> {
            Assert.assertTrue(StringUtils.isNotEmpty(result));
            System.out.println(result);
            async.complete();
        });
    }

    @Test
    public void get_counters_list_blocking() {
        String result = yandexApi.counters(RequestCounterList.of().build());

        Assert.assertTrue(StringUtils.isNotEmpty(result));

        System.out.println(result);
    }


}
