function make() {
	return {};
}

function f() {
	make();
	TAJS_dumpValue(x.p);
	make();
	TAJS_dumpValue(x.p);
}

var x = make();
x.p = 42;
f();
TAJS_dumpValue(x.p);
