/**
 * Created by MStepachev on 11.05.2016.
 */

function ReportResolver() {
    google.charts.load('current', {'packages':['line', 'corechart']});
    
    return {
        resolve: function (reportRow, bindTo) {
            if (reportRow.type) {
                //TODO[St.maxim] create a instance by a filed type
                switch (reportRow.type) {
                    case 'VISITS_DURING_MONTH':
                    {
                        $.get('/template/reports/visitsDuringMonthReport.hbs', function (source) {
                            var report = new VisitsDuringMonthReport(reportRow, source);
                            $(bindTo).append(report.render());
                        });
                        break;
                    }
                    case 'REFERRING_SOURCE':
                    {
                        $.get('/template/reports/referringSourceReport.hbs', function (source) {
                            var report = new ReferringSourceReport(reportRow, source);
                            $(bindTo).append(report.render());
                        });
                        break;
                    }
                    case 'SEARCH_PHRASE_YANDEX_DIRECT':
                    {
                        $.get('/template/reports/searchPhraseYandexDirect.hbs', function (source) {
                            var report = new SearchPhraseYandexDirect(reportRow, source);
                            $(bindTo).append(report.render());
                        });
                        break;
                    }
                }
            } else {
                throw new Error('Report type not found.');
            }
        }
    };
}
