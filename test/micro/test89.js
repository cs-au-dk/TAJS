function g(v) {
	return {a:v};
}

function h() {
	g("foo");	
}

var x = g(87);
h();
dumpValue(x.a);
assert(x.a === 87);
dumpValue(x);
dumpObject(x);
