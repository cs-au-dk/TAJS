function f() {
	//if (Math.random())
	//	throw new Error;
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
  dumpValue("HERE"); // should not appear
}
dumpValue(x); // expected: |true
