var oldClass;
var oldSel;
var selectRow = -1;
var selDate;
var selDateT;

function tempsel( row )
{
    oldClass = row.className;
    row.className = "tempsel";
}

function tempselcod_rep( row, cod )
{
    document.all.cod_rep.value=cod;
    tempsel( row );
}

function sel( row )
{
    selectRow = row.rowIndex
    if ( oldSel != null ) oldSel.className = null;
    row.className = "sel";
    oldSel = row;
    oldClass = row.className;
    selDate = row.did;
    selDateT = row.dttl;
    document.all.repTitle.innerText = "Отчёты за " + selDateT;
}

function openreport( row )
{
//  dictonary = window.open("Forms/" + row.idrep + selDate + ".HTML","report");
    dictonary = window.open("report.jsp?fileName=" + row.idrep + selDate +
        ".HTML&amp;dtTitle=" + selDateT, "report");
}

function tempunsel( row )
{
    row.className = oldClass;
}
