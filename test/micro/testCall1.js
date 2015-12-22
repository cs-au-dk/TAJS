var x1 = { m: function(x) {
	TAJS_dumpValue(this);
	TAJS_dumpState();
	return x + 1; 
} };
var x2 = { m: function(x) { 
	TAJS_dumpValue(this);
	TAJS_dumpState();
	return x + "foo"; 
} };

var x;
if (Math.random() < 0.5)
	x = x1;
else
	x = x2;

var y = x.m(7);
TAJS_dumpState();
