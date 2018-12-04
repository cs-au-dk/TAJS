function f(x) {
	return function(y) {
		x = x + 1;
		return x+y
	}
}

var g1 = f(1);
var g2 = g1(2);
var g3 = g1(2);

TAJS_assert(g2 === 4);
TAJS_assert(g3, 'isMaybeNumUInt');
TAJS_dumpValue(g2);
TAJS_dumpValue(g3);