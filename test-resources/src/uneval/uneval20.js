function f() {
	var x = 7;
	TAJS_assert(typeof g === 'undefined');
	eval("function g() {return x+1;}")
	TAJS_assert(typeof g === 'function');
	TAJS_assert(g() === 8);
}
f();
TAJS_assert(typeof g === 'undefined');
