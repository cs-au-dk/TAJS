var x = {a:42};
var y = x;
function f(q) {
	var z = {garbage: q};
	TAJS_dumpValue(z.garbage);
	z = null;
	x.a = 7;
	x = null;
}
f(123);
TAJS_assert(y.a === 7);
TAJS_dumpObject(y);
TAJS_dumpState();

