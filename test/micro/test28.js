function foo(x) {
	return x + 1;
}

function bar(y) {
	return foo(y+2)+3;
}

function baz(z) {
	return bar(z+4)+5;
}

var t = baz(6);

assert(t === 21);
dumpValue(t);

