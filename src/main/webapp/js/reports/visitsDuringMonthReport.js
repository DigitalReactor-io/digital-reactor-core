/**
 * Created by MStepachev on 11.05.2016.
 */

function VisitsDuringMonthReport(details, templateHbs) {

    var template = Handlebars.compile(templateHbs);

    function chart() {
        google.charts.load("current", {packages: ['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var ar = [["Element", "Density", {role: "style"}]];

            for (i = 0; i < details.metrics.length; i++) {
                ar.push([
                    details.metrics[i].date,
                    details.metrics[i].number,
                    details.metrics[i].dayType == "HOLIDAY" ? 'color: #EE67F7' : 'color: #76A7FA'
                ]);
            }

            var data = google.visualization.arrayToDataTable(ar);

            var view = new google.visualization.DataView(data);
            view.setColumns([0, 1,
                {
                    calc: "stringify",
                    sourceColumn: 1,
                    type: "string",
                    role: "annotation"
                },
                2]);

            var options = {
                title: "Визиты за 30 дней",
                width: 900,
                height: 300,
                bar: {groupWidth: "95%"},
                legend: {position: "none"},
            };
            var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
            chart.draw(view, options);
        }

    }

    function description() {
        return 'description';
    }

    function reason() {
        return details.reason;
    }

    return {
        render: function () {
            var templateData = {
                description: description(),
                reason: reason()
            };

            chart();

            return template(templateData);
        }
    };

}
