var x = "foo";

function g() {
	x = 1;
}

function h() {
	g();
	x = true;
}

h();
TAJS_dumpValue(x); // expected: true
