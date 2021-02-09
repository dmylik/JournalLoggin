function GetDate()
{
    var selind_1 = document.getElementById("monthSel").options.selectedIndex;
    document.getElementById("in1").value = document.getElementById("monthSel").options[selind_1].text;

    var selind_2 = document.getElementById("yearSel").options.selectedIndex;
    document.getElementById("in2").value = document.getElementById("yearSel").options[selind_2].text;
}

function filterOpen() {
    document.getElementById("content_per").style.display = "block";
}

function filterClose() {
    document.getElementById("content_per").style.display = "none";
}

function extracted() {
    var resultSet = [];
    var table = document.getElementById("per");
    var tableRows = table.rows;

    for (var i = 1; i < tableRows.length; i++) {
        if (tableRows[i].cells[0].children[0].checked) {
            resultSet.push(tableRows[i].innerText);
            tableRows[i].cells[0].children[0].checked = false;
        }
    }

    document.getElementById("inpCheck").checked = false;

    return resultSet;
}
// Добавление indexOf в прототип (для Internet Explorer 8)
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(elt /*, from*/) {
        var len = this.length >>> 0;

        var from = Number(arguments[1]) || 0;
        from = (from < 0)
            ? Math.ceil(from)
            : Math.floor(from);
        if (from < 0)
            from += len;

        for (; from < len; from++) {
            if (from in this &&
                this[from] === elt)
                return from;
        }
        return -1;
    };
}

var listData;
function loadTableInVar() {
    listData = document.getElementById("tabl").rows;
}

function resultFilter() {
    var checkedPerev = extracted();

    if (checkedPerev.length === 0) {
        document.getElementById("content_per").style.display = "none";
        return;
    }

    var newTable = "<table id='tabl' width='100%' border='1' style='border-collapse: collapse'>";
    var cellsTh = listData[0].cells;

    // заголовок таблицы
    newTable += "<tr><th width='10%' style='border: outset 4px #bceeff' onclick='filterOpen()'>" + listData[0].cells[0].innerText + "</th>";
    for (var i = 1; i  < cellsTh.length; i++) {
        newTable += "<th>" + listData[0].cells[i].innerText + "</th>";
    }
    newTable += "</tr>";

    // тело таблицы
    for (var m = 1; m < listData.length; m++) {
        if (checkedPerev.indexOf(listData[m].cells[0].innerText) !== -1) {
            listData[m].cells[1].innerText.indexOf("Всего") !== -1 ? newTable += "<tr style='font-weight: bold'>" : newTable += "<tr>";
            for (var k = 0; k < listData[m].cells.length; k++) {
                newTable += "<td width='10%'>" + listData[m].cells[k].innerText + "</td>";
            }
            newTable += "</tr>";
        }
    }

    newTable += "</table>";

    document.getElementById("content_per").style.display = "none";
    document.getElementById("tablePrint").removeChild(document.getElementById("tabl"));
    document.getElementById('tablePrint').innerHTML = newTable;
}

function choiceAll() {
    var table = document.getElementById("per");
    var tableRows = table.rows;

    if (document.getElementById("inpCheck").checked) {
        for (var i = 1; i < tableRows.length; i++) {
            tableRows[i].cells[0].children[0].checked = true;
        }
    } else {
        for (var j = 1; j < tableRows.length; j++) {
            tableRows[j].cells[0].children[0].checked = false;
        }
    }

}



