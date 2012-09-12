function f(x) {
	if (x <= 1)
		return 1;
	else
		return f(x-1) + f(x-1);
}

var q = f(3);
assert(q === 4);
dumpValue(q);
