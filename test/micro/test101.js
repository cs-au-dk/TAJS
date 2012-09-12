var x;
if (Math.random()) {
	x = {a:RegExp};
} else {
	x = {b:Date};
}

dumpValue(x.a);

var temp = x.a;
if (temp != null) {
	assumeNonNullUndef("temp");
	dumpValue(temp); // expected: [REGEXP]  (not Undef!!!!)
}
	