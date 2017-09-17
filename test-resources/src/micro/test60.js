function yell(n) { 
    return n > 0 ? yell(n-1) + 10 : 20; 
 } 

var x = yell(4);

TAJS_dumpValue(x);

//assert(x === 60);

