function f() {
	//if (Math.random())
		throw new Error;
}

var x = "foo";

function g() {
	f();
	x = 1;
}

function h() {
	g();
	x = true;
}

try{
  h();
} catch(ex) {
  TAJS_dumpValue("HERE"); // should appear
}
TAJS_dumpValue(x); // expected: "foo"
