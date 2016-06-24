package io.digitalreactor.core.api.yandex.model;

import org.junit.Test;

import static io.digitalreactor.core.api.yandex.model.AbstractRequest.GOALS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by MStepachev on 22.06.2016.
 */
public class RequestGoalsTest {

    @Test
    public void toQuery_counterIdAndTokenAndPrefix_builtUrl() {
        Long counterId = 1234L;
        String token = "some_token";

        String url = RequestGoals.of()
                .prefix(GOALS)
                .counterId(counterId)
                .token(token)
                .build()
                .toQuery();

        assertThat(url, is(equalTo(GOALS + counterId + "/goals?oauth_token=" + token)));
    }
}