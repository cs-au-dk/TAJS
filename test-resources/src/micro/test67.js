var a = 1;
var b = 2;
var c;
if (a < b)
	c = 42;
var d;
if (a == b)
	d = 43;
else
	d = 87;
var e;
if (a != b)
	e = 44;
else
	e = 98;
TAJS_dumpValue(c);
TAJS_dumpValue(d);
TAJS_dumpValue(e);
