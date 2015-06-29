var x = {}
var p;
if (Math.random()) 
	p = "foo";
else
	p = "bar";
var y = x[p];
TAJS_dumpValue(y);
var q;
if (y == null)
	q = true;
else
	q = false;

TAJS_dumpValue(p);
TAJS_dumpValue(y);
TAJS_dumpValue(q);
