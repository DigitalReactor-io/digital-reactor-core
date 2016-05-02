/**
 * Created by ingvard on 02.05.16.
 */

var selectedCounter = null;
var buttonIsDisable = true;

$(".project-select ul li").click(function () {
    selectedCounter = $(this).data("id");

    $(".project-select ul li").removeClass("active");
    $(this).addClass("active");

    $("#finish-button").removeClass("registration-button-disable");
    buttonIsDisable = false;
});

$("#finish-button").click(function () {
    if (!buttonIsDisable) {
        buttonIsDisable = false;

        $.post("/registration/finish", {counterId: selectedCounter})
            .done(function (data) {
                $(".modal-window-padding").show("slow");
            });
    }
});