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
        });
    },
    xhrFields: {
        withCredentials: true
    }
});




