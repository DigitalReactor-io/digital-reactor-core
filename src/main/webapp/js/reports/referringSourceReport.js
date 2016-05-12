/**
 * Created by MStepachev on 12.05.2016.
 */

function ReferringSourceReport(details, templateHbs) {
    var template = Handlebars.compile(templateHbs);

    function chart() {

        google.charts.setOnLoadCallback(drawReferringSourceChart);

        function drawReferringSourceChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Day');

            var maxLength = 0;

            for (i = 0; i < details.sources.length; i++) {
                maxLength = Math.max(maxLength, details.sources[i].metrics.length);
                data.addColumn('number', details.sources[i].name);
            }

            var chartDots = [];

            for (i = 0; i < maxLength; i++) {
                var row = [];
                row.push(details.sources[0].metrics[i].date);
                for (j = 0; j < details.sources.length; j++) {
                    row.push(details.sources[j].metrics[i].number);
                }
                chartDots.push(row);
            }

            data.addRows(chartDots);

            var options = {
                chart: {
                    title: 'Ситочники трафика'
                },
                interpolateNulls: true,
                width: 900,
                height: 300
            };

            var chart = new google.charts.Line(document.getElementById("referring-source-chart"));

            chart.draw(data, options);
        }

    }

    function description() {
        switch (details.action) {
            case 'INCREASING':
            {
                return "Конверсия сайта увеличилась на " + details.totalConversionChange + "% и составила " + details.totalConversion + "%."
            }
            case 'DECREASING':
            {
                return "Конверсия сайта уменьшилась на " + details.totalConversionChange + "% и составила " + details.totalConversion + "%."
            }
            case 'UNALTERED':
            {
                return "Конверсия сайта не изменилась и составила " + details.totalConversion + "%."
            }
        }
    }

    return {
        render: function () {
            var options = {
                sources: details.sources,
                description: description(),
                goals: details.totalGoals
            };

            chart();

            return template(options);
        }
    }

}