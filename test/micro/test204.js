var a = ["foo", "bar", "baz"];
var x = "";

function f() {
	for (var i = 0; i < a.length; i++) {
		dumpValue(i);
		x += a[i];
	}
}
TAJS_addContextSensitivity(f, "i")

if (Math.random()) a[100] = "dyt"; // also try with 2 instead of 100 :-)
dumpObject(a)

f();
dumpValue(x);
