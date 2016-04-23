package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.reports.ReportLoader;
import io.digitalreactor.core.domain.reports.VisitReportLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ingvard on 10.04.16.
 */
public class ReportLoaderFactory {

    private static Map<ReportTypeEnum, Class> handlers = new HashMap<>();

    static {
        handlers.put(ReportTypeEnum.VISITS_DURING_MONTH, VisitReportLoader.class);
    }

    public static ReportLoader instance(ReportTypeEnum reportType) {


        return null;
        //return Class.forName(handlers.get(reportType)).newInstance();

    }
}
