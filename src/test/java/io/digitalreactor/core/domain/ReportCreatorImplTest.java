package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceReportDto;
import io.digitalreactor.core.gateway.api.dto.SearchPhraseYandexDirectDto;
import io.digitalreactor.core.gateway.api.dto.VisitDto;
import io.digitalreactor.core.gateway.api.dto.VisitsDuringMonthReportDto;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by flaidzeres on 12.06.2016.
 */
@Ignore
public class ReportCreatorImplTest {

    private final ReportCreator reportCreator = new ReportCreatorImpl();


    @Test
    public void createReferringSourceReport() throws IOException {
        String raw = FileUtils.readFileToString(new File("src/test/resources/visits.json"));
        ReportMessage message = new ReportMessage();
        message.raw = raw;

        List<VisitDto> visitDtos = new ArrayList<>();

        visitDtos.add(new VisitDto(1, "2015-01-01", VisitDto.DayType.WEEKDAY));
        visitDtos.add(new VisitDto(2, "2015-01-02", VisitDto.DayType.WEEKDAY));
        visitDtos.add(new VisitDto(3, "2015-01-03", VisitDto.DayType.HOLIDAY));
        visitDtos.add(new VisitDto(4, "2015-01-04", VisitDto.DayType.HOLIDAY));
        visitDtos.add(new VisitDto(5, "2015-01-05", VisitDto.DayType.WEEKDAY));
        visitDtos.add(new VisitDto(6, "2015-01-06", VisitDto.DayType.WEEKDAY));
        visitDtos.add(new VisitDto(7, "2015-01-07", VisitDto.DayType.WEEKDAY));

        VisitsDuringMonthReportDto expectedVisits = new VisitsDuringMonthReportDto(28, 0, null, visitDtos, null);

        final VisitsDuringMonthReportDto actualVisits = reportCreator.createVisitsDuringMothReport(message);

        ReflectionAssert.assertReflectionEquals(expectedVisits, actualVisits);
    }

    @Test
    public void createVisitsDuringMothReport() throws IOException {
        String raw = FileUtils.readFileToString(new File("src/test/resources/refereceSources.json"));
        ReportMessage message = new ReportMessage();
        message.raw = raw;

        final ReferringSourceReportDto referringSourceReport = reportCreator.createReferringSourceReport(message);

        Assert.assertNotNull(referringSourceReport);
    }

    @Test
    public void createSearchPhraseYandexReport() throws IOException {
        String raw = FileUtils.readFileToString(new File("src/test/resources/directSearchPhrase.json"));
        ReportMessage message = new ReportMessage();
        message.raw = raw;

        SearchPhraseYandexDirectDto searchPhraseYandexReport = reportCreator.createSearchPhraseYandexReport(message);

        assertThat(searchPhraseYandexReport, is(equalTo(null)));
    }

    @Test
    public void chooseValueOfWeekAndLastWeek_fullSequence_twoSubSequenceByCalendarWeek() {
        List<Double> metrics = Arrays.asList(1., 2., 3., 4., 5., 6., 7., 8., 9., 10., 11., 12., 13., 14., 15.);
        List<Double> secondWeek = Arrays.asList(1., 2., 3., 4., 5., 6., 7.);
        List<Double> firstWeek = Arrays.asList(8., 9., 10., 11., 12., 13., 14.);
        String date = "2016-06-20";

        List<List<Double>> twoWeek = ((ReportCreatorImpl) reportCreator).chooseValueOfWeekAndLastWeek(metrics, LocalDate.parse(date));

        assertThat(twoWeek.size(), is(equalTo(2)));
        assertThat(twoWeek.get(0), is(equalTo(secondWeek)));
        assertThat(twoWeek.get(1), is(equalTo(firstWeek)));
    }

    @After
    public void after() {

    }
}