var t;
if (Math.random()) 
	t = Object;
else
	t = Boolean;
var x = new t;
dumpValue(t);
dumpValue(x);
dumpValue(Object);
dumpValue(new Object);
x.foo = 42;
dumpObject(x);
