function f(x) {
	return function(y) {
		x = x + 1;
		return x+y
	}
}

var g1 = f(1);
var g2 = g1(2);
var g3 = g1(2);

assert(g2 === 4);
assert(g3 == 5);
dumpValue(g2);
dumpValue(g3);