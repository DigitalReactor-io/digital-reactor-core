package io.digitalreactor.core.api.yandex;

import io.digitalreactor.core.api.yandex.model.Request;
import io.digitalreactor.core.domain.messages.ReportMessage;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static io.digitalreactor.core.api.yandex.model.AbstractRequest.DATA;
import static io.digitalreactor.core.api.yandex.model.AbstractRequest.DATA_BY_TYPE;
import static io.digitalreactor.core.domain.ReportTypeEnum.*;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public class RequestBuilder {

    private static final SimpleDateFormat dateFormater = new SimpleDateFormat("YYYY-MM-DD");

    public static Request createRequest(ReportMessage reportMessage) {
        final Request.Builder builder = Request.of();
        LocalDate toDay = LocalDate.now();
        LocalDate before30days = toDay.minusDays(30);

        //TODO[St.maxim] was problem with date in the last version
        builder
                .date1(before30days.toString())
                .date2(toDay.toString());

        if (VISITS_DURING_MONTH.equals(reportMessage.reportType)) {
            return builder
                    .prefix(DATA_BY_TYPE)
                    .ids(reportMessage.counterId)
                    .metrics("ym:s:visits")
                    .group("day")
                    .token(reportMessage.clientToken)
                    .build();
        } else if (REFERRING_SOURCE.equals(reportMessage.reportType)) {
            return builder
                    .prefix(DATA_BY_TYPE)
                    .ids(reportMessage.counterId)
                    .metrics("ym:s:visits")
                    .group("day")
                    .dimensions("ym:s:<attribution>TrafficSource")
                    .attribution("last")
                    .token(reportMessage.clientToken)
                    .build();
        } else if (SEARCH_PHRASE_YANDEX_DIRECT.equals(reportMessage.reportType)) {
            return builder
                    .prefix(DATA)
                    .ids(reportMessage.counterId)
                    .dimensions("ym:s:directSearchPhrase")
                    .metrics("ym:s:visits", "ym:s:pageDepth", "ym:s:avgVisitDurationSeconds", "ym:s:bounceRate")
                    .token(reportMessage.clientToken)
                    .limit(10000)
                    .build();
        }
        throw new RuntimeException("can't create request because type doesn't not exist:" + reportMessage.reportType);
    }
}
