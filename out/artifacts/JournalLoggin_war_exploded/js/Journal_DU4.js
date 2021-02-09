function setValueForInput() {
    var pr = document.getElementById("ch_pr");
    var ot = document.getElementById("ch_ot");

    var inputP = document.getElementById("inP");
    var inputO = document.getElementById("inO");

    if (pr.checked) {
        inputP.value = "checked";
    } else {
        inputP.value = " ";
    }

    if (ot.checked) {
        inputO.value = "checked";
    } else {
        inputO.value = " ";
    }
}
