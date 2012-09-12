var y = 0
function f(x) {
	eval("y = " + x);
}
eval("y = 34")
dumpValue(y)
f(100)
dumpValue(y)