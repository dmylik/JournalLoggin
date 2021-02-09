
function comboSwith(thing, add)
{
    ind = thing.selectedIndex + add;
    if(ind < 0) ind=thing.length - 1;
    if(ind >= thing.length) ind=0;
    thing[ind].selected = "true";
}
