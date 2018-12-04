var x;
if (Math.random()) 
  x = null;
else if (Math.random()) 
  x = {a:42};
else
  x = {a:42, b:{c:87}};

TAJS_dumpValue(x);
var y1 = x.a;
var y2 = x.a;
TAJS_dumpValue(y1);
TAJS_dumpValue(y2);
TAJS_dumpValue(x);

TAJS_dumpValue(x.b);
var y3 = x.b.c;
var y4 = x.b.c;
TAJS_dumpValue(y3);
TAJS_dumpValue(y4);
TAJS_dumpValue(x.b);
