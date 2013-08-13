var x1 = { m: function(x) {
	dumpValue(this);
	dumpState();
	return x + 1; 
} };
var x2 = { m: function(x) { 
	dumpValue(this);
	dumpState();
	return x + "foo"; 
} };

var x;
if (Math.random() < 0.5)
	x = x1;
else
	x = x2;

var y = x.m(7);
dumpState();
