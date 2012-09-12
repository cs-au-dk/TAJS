function make() {
	return {};
}

function f() {
	make();
	dumpValue(x.p);
}

var x = make();
x.p = 42;
f();
