var stDate;
var finDate;

function d1() {
    $(function () {

        stDate = $("#datep1").datepicker("getDate");
        // if (!$("#datep2").datepicker("isDisabled")) {
            $("#datep2")
                // .datepicker("setDate", stDate)
                .datepicker("option", "minDate", stDate)
                .datepicker("option", "maxDate", diff());
            // $("#datep1").datepicker("disable");
        // }

    });
}

function d2() {
    $(function () {
        finDate = $("#datep2").datepicker("getDate");
        // if (!$("#datep1").datepicker("isDisabled")) {
            $("#datep1")
                // .datepicker("setDate", finDate)
                .datepicker("option", "maxDate", finDate)
                .datepicker("option", "minDate", -((Date.now() - finDate.getTime() + 31*24*60*60*1000)/(24*60*60*1000)) );
            // $("#datep2").datepicker("disable");
        // }

    });
}

var diff = function () {
    if ((Date.now() - stDate.getTime())/(24*60*60*1000) < 31) {
        return "+0"
    } else {
        return -((Date.now() - stDate.getTime() - 31*24*60*60*1000)/(24*60*60*1000));
    }
};

