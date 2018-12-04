function f(x) {
	//TAJS_dumpState();
	return x + 1;
}

var q = f(7);

TAJS_assert(q == 8);
TAJS_dumpValue(q);

var obj = {aaa:7}
TAJS_assert(obj.aaa === 7);
TAJS_dumpValue(obj.aaa);
