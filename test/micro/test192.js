function f(x) {
	if (x > 5)
		return f(x - 1);
	else
		return 42;
}

var t = f(10);
dumpState();
