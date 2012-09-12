var fib = function f(x) {
	dumpValue(x)
	if (x <= 1)
		return 1;
	else
		return f(x-1) + f(x-2);
}

var t = fib(3);
dumpValue(t);
