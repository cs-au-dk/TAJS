var y = [1,2,3]
if (Math.random())
    var x = 1000
else 
    var x = 0
dumpValue(x);

y.length = x;
dumpObject(y);
y[45] = 10;
dumpObject(y);
y.length = 6;
dumpObject(y);
