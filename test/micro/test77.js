try { // duh... Rhino "optimizes" try-catch blocks by removing them if the try part is empty :-(
    var xx = 10 + 10;
    TAJS_dumpValue(xx)
} 
catch (ee) {
    TAJS_dumpValue(ee) //shouldn't be printed
}
TAJS_dumpValue(xx);
