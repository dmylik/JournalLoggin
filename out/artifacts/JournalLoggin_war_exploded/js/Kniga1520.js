var stDate;

// function d1() {
//     $(function () {
//
//         stDate = $("#knigaDP_1").datepicker("getDate");
//         $("#knigaDP_2")
//             .datepicker("option", "minDate", stDate)
//             .datepicker("option", "maxDate", diff());
//     });
// }
//
// var diff = function () {
//     if ((Date.now() - stDate.getTime())/(24*60*60*1000) < 1600) {
//         return "+0"
//     } else {
//         return -((Date.now() - stDate.getTime() - 1600*24*60*60*1000)/(24*60*60*1000));
//     }
// };

function setValueForInputKlassOp() {
    var type_1 = document.getElementById("type_1");
    var type_2 = document.getElementById("type_2");

    var typeHid_1 = document.getElementById("typeHid_1");
    var typeHid_2 = document.getElementById("typeHid_2");

    if (type_1.checked) {
        typeHid_1.value = "checked";
    } else {
        typeHid_1.value = " ";
    }

    if (type_2.checked) {
        typeHid_2.value = "checked";
    } else {
        typeHid_2.value = " ";
    }
}

function setValueForInputKat() {
    var kt_1 = document.getElementById("kt_1");
    var kt_2 = document.getElementById("kt_2");

    var ktHid_1 = document.getElementById("ktHid_1");
    var ktHid_2 = document.getElementById("ktHid_2");

    if (kt_1.checked) {
        ktHid_1.value = "checked";
    } else {
        ktHid_1.value = " ";
    }

    if (kt_2.checked) {
        ktHid_2.value = "checked";
    } else {
        ktHid_2.value = " ";
    }
}

function setValueForNumvag() {
    var numVag = document.getElementById("vagId");
    if (numVag.value === "") {
        document.getElementById("vagId").value = ".";
    }
}


