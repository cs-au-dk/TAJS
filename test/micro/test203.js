var a = ["foo", "bar", "baz"];
var x = "";

function f() {
	for (var i = 0; i < a.length; i++) {
		TAJS_dumpValue(i);
		x += a[i];
	}
}
TAJS_addContextSensitivity(f, "i")

f();
TAJS_dumpValue(x);
