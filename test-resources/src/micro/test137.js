function f() {
}

function g() {
	var ttttt = x;
	TAJS_dumpValue(ttttt);
	x = f();
	TAJS_dumpValue(ttttt); // should be same as above
}

var x = "dyt";
g();
x = 42;
g();
