var fib = function f(x) {
	TAJS_dumpValue(x)
	if (x <= 1)
		return 1;
	else
		return f(x-1) + f(x-2);
}

var t = fib(3);
TAJS_dumpValue(t);
