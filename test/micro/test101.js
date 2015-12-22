var x;
if (Math.random()) {
	x = {a:RegExp};
} else {
	x = {b:Date};
}

TAJS_dumpValue(x.a);

var temp = x.a;
if (temp != null) {
	TAJS_assert(temp !== undefined);
}
	