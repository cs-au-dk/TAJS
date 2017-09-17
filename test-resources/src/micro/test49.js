var y = [1,2,3]
if (Math.random())
    var x = 1000
else 
    var x = 0
TAJS_dumpValue(x);

y.length = x;
TAJS_dumpObject(y);
y[45] = 10;
TAJS_dumpObject(y);
y.length = 6;
TAJS_dumpObject(y);
