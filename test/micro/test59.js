function f(x) {
	if (x)
	  return "a";
	else
	  return "b";
}

var a = f(true) + f(false);
TAJS_dumpValue(a);
TAJS_assert(a, 'isMaybeStrPrefixedIdentifierParts');

a = 'ab'
var x = {};
x[a] = "hello";

TAJS_dumpObject(x);

TAJS_dumpValue(x.dfg);
TAJS_dumpValue(x[1]);
TAJS_dumpValue(x[2]);
TAJS_dumpValue(x.ab);
TAJS_assert(x.dfg === undefined);
TAJS_assert(x[1] === undefined);
TAJS_assert(x[2] === undefined);
TAJS_assert(x.ab === "hello");



