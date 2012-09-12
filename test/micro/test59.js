function f(x) {
	if (x)
	  return "a";
	else
	  return "b";
}

var a = f(true) + f(false);
dumpValue(a);
assert(a === "ab");

var x = {};
x[a] = "hello";

dumpObject(x);

dumpValue(x.dfg);
dumpValue(x[1]);
dumpValue(x[2]);
dumpValue(x.ab);
assert(x.dfg === undefined);
assert(x[1] === undefined);
assert(x[2] === undefined);
assert(x.ab === "hello");



