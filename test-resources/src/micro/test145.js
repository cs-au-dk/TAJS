function g(v) {
	return {a:v};
}

var x = g(87);

function h() {
	x.a = true;
	g("foo");	
}

h();

TAJS_dumpValue(x);
TAJS_dumpObject(x);

