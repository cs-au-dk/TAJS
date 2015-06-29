function foo(x) {
	return x + 777;
}

var q = foo(1234) * 5678;

TAJS_assert (q === 11418458);
