function f() {
	var x = 7;
	eval("function g() {return x+1;}")
	TAJS_dumpValue(x);
	TAJS_dumpValue(g);
	TAJS_dumpValue(g());
}
f();
TAJS_dumpValue(g);
