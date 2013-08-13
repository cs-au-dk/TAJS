var x = {foo: 1, bar: 2};

var y,z;

for (var p in x) {
	var t = {a: x[p]};
	if (p == "foo")
		y = t;
	else
		z = t;
}

dumpValue(y);
dumpValue(z);
dumpObject(y);
dumpObject(y);

z.a = "dyt";

dumpValue(y.a);
dumpValue(z.a);

assert(y.a === 1);
assert(z.a === "dyt");
