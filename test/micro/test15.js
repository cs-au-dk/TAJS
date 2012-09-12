function f(x) {
	//dumpState();
	return x + 1;
}

var q = f(7);

assert(q == 8);
dumpValue(q);

var obj = {aaa:7}
assert(obj.aaa === 7);
dumpValue(obj.aaa);
