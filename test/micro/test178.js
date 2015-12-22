var obj = {}
var y = "bla";
if (obj.foo)
	y = 42;
TAJS_dumpValue(y);

if (Math.random())
  obj.foo = "dsg";
if (obj.foo)
	y = true;
TAJS_dumpValue(y);

