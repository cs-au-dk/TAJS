function f(x) {
	var y = x + 7;
	return function(z) {
		y = y + 1;
		return x+y+z;
	}
}

var g1 = f(1);
var g2 = g1(2);

assert(g2 == 12);
dumpValue(g2);
