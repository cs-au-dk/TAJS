function make() {
	return {}
}

var x = make();
x.a = 7;
x.b = x;

if (Math.random()) {
	var y = make();
	y.a = "foo";
	y.c = y
	y.d = x;
} else {
	
}

dumpModifiedState();