var x;
if (Math.random()) 
  x = null;
else if (Math.random()) 
  x = {a:42};
else
  x = {a:42, b:{c:87}};

dumpValue(x);
var y1 = x.a;
var y2 = x.a;
dumpValue(y1);
dumpValue(y2);
dumpValue(x);

dumpValue(x.b);
var y3 = x.b.c;
var y4 = x.b.c;
dumpValue(y3);
dumpValue(y4);
dumpValue(x.b);
