try { // duh... Rhino "optimizes" try-catch blocks by removing them if the try part is empty :-(
    var xx = 10 + 10;
    dumpValue(xx)
} 
catch (ee) {
    dumpValue(ee) //shouldn't be printed
}
dumpValue(xx);
