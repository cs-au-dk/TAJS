var t;
if (Math.random()) 
	t = Object;
else
	t = Boolean;
var x = new t;
TAJS_dumpValue(t);
TAJS_dumpValue(x);
TAJS_dumpValue(Object);
TAJS_dumpValue(new Object);
x.foo = 42;
TAJS_dumpObject(x);
