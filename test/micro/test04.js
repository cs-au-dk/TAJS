var z = {}
dumpValue(z);

var x = { abc: 99999, def: "dfgcfdc", qqqqq: null, wwwww: undefined, e: true, rrrr: z };
dumpValue(x.qqqqq);
dumpValue(x.rrrr);
dumpValue(x.e);
dumpValue(x.abc);
dumpValue(x.def);

var x = {foo: 12345};
dumpValue(x.foo + 5678);

var bar = function() {
	return 777;
}
var foo = bar() * 1234;
dumpValue(foo);

var q = (function(x) {
	return x + 777;
})(1234) * 5678;
dumpValue("x" + q);

var bar = {def: 88888}.def * 45678;
dumpValue(bar);

var xyz = { abc: 99999 }
dumpValue(xyz.abc);
