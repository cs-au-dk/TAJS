function g(v) {
	return {a:v};
}

function h() {
	g("foo");	
}

var x = g(87);
h();
TAJS_dumpValue(x.a);
TAJS_assert(x.a === 87);
TAJS_dumpValue(x);
TAJS_dumpObject(x);
