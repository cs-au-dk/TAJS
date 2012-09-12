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
dumpValue(g1);
dumpValue(g2);
dumpValue(g3);
assert(g3 === 6);
