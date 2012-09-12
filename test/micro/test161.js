var x = {a:42};
var y = x;
function f(q) {
	var z = {garbage: q};
	dumpValue(z.garbage);
	z = null;
	x.a = 7;
	x = null;
}
f(123);
assert(y.a === 7);
dumpObject(y);
dumpState();

