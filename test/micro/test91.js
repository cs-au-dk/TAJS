var x = {}
var p;
if (Math.random()) 
	p = "foo";
else
	p = "bar";
var y = x[p];
dumpValue(y);
var q;
if (y == null)
	q = true;
else
	q = false;

dumpValue(p);
dumpValue(y);
dumpValue(q);
