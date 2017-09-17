var x = [5,3,8];
var c1 = 0;
var c2 = 0;
x.sort(function(a,b){ if (c1>0) c2++; c1++; return a - b; });
TAJS_dumpObject(x);
TAJS_dumpValue(c1);
TAJS_dumpValue(c2);
