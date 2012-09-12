function f1() {
	var x = {}
	x.a = 7;
	f2({b:x});
	return x.a;
}

function f2(y) {
	f3(y);
}

function f3(y) {
	f4(y);
	y.b = null;
}

function f4(y) {
	y.b.a = true;
}

dumpValue(f1());
