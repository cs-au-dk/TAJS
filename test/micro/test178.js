var obj = {}
var y = "bla";
if (obj.foo)
	y = 42;
dumpValue(y);

if (Math.random())
  obj.foo = "dsg";
if (obj.foo)
	y = true;
dumpValue(y);

