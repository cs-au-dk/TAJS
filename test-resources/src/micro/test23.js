function f(x) {
	return function(y) {
		return function(z) {
			return x+y+z
		}
	}
}

var g1 = f(1);
var g2 = g1(2);
var g3 = g2(3);
TAJS_dumpValue(g1);
TAJS_dumpValue(g2);
TAJS_dumpValue(g3);
TAJS_assert(g3 === 6);
