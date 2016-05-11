/**
 * Created by MStepachev on 11.05.2016.
 */

function ReportResolver() {
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
                    }
                }
            } else {
                throw new Error('Report type not found.');
            }
        }
    };
}
