function f(x) {
	return g(x);
	function g(y) {return y+1}; 
}

var a = f(1);

assert(a == 2);
dumpValue(a);
