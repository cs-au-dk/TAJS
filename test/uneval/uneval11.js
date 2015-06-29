var y = 0
function f(x) {
	eval("y = " + x);
}
eval("y = 34")
TAJS_dumpValue(y)
f(100)
TAJS_dumpValue(y)