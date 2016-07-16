/**
 * Created by ingvard on 10.05.16.
 */

var apiUrl = '/api/v1/projects';


$.ajax({
    url: apiUrl,
    type: 'get',
    dataType: 'json',
    success: function (projects) {
        $.get('/template/project-list.hbs', function (source) {
            var template = Handlebars.compile(source);
            var html = template({projects: projects});

            $("#projects").html(html);

            $(".project-show-action-button").on("click", function () {
                var self = this;
                $(this).hide();
                var projectId = $(this).data("project-id");
                $.ajax({
                    url: apiUrl + "/" + projectId + "/updateSummary",
                    type: 'PUT',
                    success: function () {
                        $(self).show();
                    }
                });
            });
        });
    },
    xhrFields: {
        withCredentials: true
    }
});




