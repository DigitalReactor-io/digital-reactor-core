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
        DateTime toDay = DateTime.now();
        DateTime before30days = toDay.minusDays(30);

        builder
                .date1(dateFormater.format(before30days))
                .date2(dateFormater.format(toDay));

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

            LocalDate dateNow = LocalDate.now();
            LocalDate dateMonthAgo = dateNow.minusDays(30);

            return builder
                    .prefix(DATA)
                    .ids(reportMessage.counterId)
                    .dimensions("ym:s:directSearchPhrase")
                    .metrics("ym:s:visits", "ym:s:pageDepth", "ym:s:avgVisitDurationSeconds", "ym:s:bounceRate")
                    .token(reportMessage.clientToken)
                    .limit(10000)
                    .date1(dateNow.toString())
                    .date2(dateMonthAgo.toString())
                    .build();
        }
        throw new RuntimeException("can't create request because type doesn't not exist:" + reportMessage.reportType);
    }
}
