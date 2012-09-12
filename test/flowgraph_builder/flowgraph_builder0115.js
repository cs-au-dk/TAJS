function f(x) {
	return g();
	function g() { return x}
}
f(10)

