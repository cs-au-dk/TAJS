function g(v) {
	return {a:v};
}

function h() {
	g("foo");	
}

var x = g(87);
h();
dumpValue(x.a);
//dumpObject(x);
