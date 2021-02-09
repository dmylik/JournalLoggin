var stDate;
var finDate;

function d1() {
    $(function () {

        stDate = $("#datep1").datepicker("getDate");
        $("#datep2")
            .datepicker("option", "minDate", stDate)
            .datepicker("option", "maxDate", diff());
    });
}

function d2() {
    $(function () {
        finDate = $("#datep2").datepicker("getDate");
        $("#datep1")
            .datepicker("option", "maxDate", finDate)
            .datepicker("option", "minDate", -((Date.now() - finDate.getTime() + 30*24*60*60*1000)/(24*60*60*1000)) );
    });
}

var diff = function () {
    if ((Date.now() - stDate.getTime())/(24*60*60*1000) < 30) {
        return "+0"
    } else {
        return -((Date.now() - stDate.getTime() - 30*24*60*60*1000)/(24*60*60*1000));
    }
};

