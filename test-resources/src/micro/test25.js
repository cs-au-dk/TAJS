function f(x) {
	var y = x + 7;
	return function(z) {
		y = y + 1;
		return x+y+z;
	}
}

var g1 = f(1);
var g2 = g1(2);

TAJS_assert(g2 == 12);
TAJS_dumpValue(g2);
