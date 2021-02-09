var stDate;

function d1() {
    $(function () {

        stDate = $("#dp_1").datepicker("getDate");
        $("#dp_2")
            .datepicker("option", "minDate", stDate)
            .datepicker("option", "maxDate", diff());
    });
}

var diff = function () {
    if ((Date.now() - stDate.getTime())/(24*60*60*1000) < 30) {
        return "+0"
    } else {
        return -((Date.now() - stDate.getTime() - 30*24*60*60*1000)/(24*60*60*1000));
    }
};

