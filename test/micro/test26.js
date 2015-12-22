function f(x) {
	return g(x);
	function g(y) {return y+1}; 
}

var a = f(1);

TAJS_assert(a == 2);
TAJS_dumpValue(a);
