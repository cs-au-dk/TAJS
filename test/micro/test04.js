var z = {}
TAJS_dumpValue(z);

var x = { abc: 99999, def: "dfgcfdc", qqqqq: null, wwwww: undefined, e: true, rrrr: z };
TAJS_dumpValue(x.qqqqq);
TAJS_dumpValue(x.rrrr);
TAJS_dumpValue(x.e);
TAJS_dumpValue(x.abc);
TAJS_dumpValue(x.def);

var x = {foo: 12345};
TAJS_dumpValue(x.foo + 5678);

var bar = function() {
	return 777;
}
var foo = bar() * 1234;
TAJS_dumpValue(foo);

var q = (function(x) {
	return x + 777;
})(1234) * 5678;
TAJS_dumpValue("x" + q);

var bar = {def: 88888}.def * 45678;
TAJS_dumpValue(bar);

var xyz = { abc: 99999 }
TAJS_dumpValue(xyz.abc);
