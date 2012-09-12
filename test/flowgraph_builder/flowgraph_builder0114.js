function f(x) {
	return g();
	function g() {return x+2}
}

f(10)

