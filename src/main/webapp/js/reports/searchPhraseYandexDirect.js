/**
 * Created by MStepachev on 12.05.2016.
 */

function SearchPhraseYandexDirect(details, templateHbs) {
    var template = Handlebars.compile(templateHbs);

    return {
        render: function () {

            return template(details);
        }
    }

}