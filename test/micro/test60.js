function yell(n) { 
    return n > 0 ? yell(n-1) + 10 : 20; 
 } 

var x = yell(4);

dumpValue(x);

//assert(x === 60);

