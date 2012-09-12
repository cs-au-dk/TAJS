function g(v) {
	return {a:v};
}

var x = g(87);

function h() {
	x.a = true;
	g("foo");	
}

h();

dumpValue(x);
dumpObject(x);

