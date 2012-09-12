function f() {
	var x = 7;
	eval("function g() {return x+1;}")
	dumpValue(x);
	dumpValue(g);
	dumpValue(g());
}
f();
dumpValue(g);
