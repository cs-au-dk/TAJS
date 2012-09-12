function f() {
}

function g() {
	var ttttt = x;
	dumpValue(ttttt);
	x = f();
	dumpValue(ttttt); // should be same as above
}

var x = "dyt";
g();
x = 42;
g();
