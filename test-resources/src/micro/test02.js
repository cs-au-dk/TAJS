var foo = function(x) {
	return x + 42;
}

var x = foo(2222);

TAJS_assert(x == 2264);
