function f(x) {
	if (x <= 1)
		return 1;
	else
		return f(x-1) + f(x-1);
}

var q = f(3);
TAJS_assert(q, 'isMaybeNumUInt');
TAJS_dumpValue(q);
