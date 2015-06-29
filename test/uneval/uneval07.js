var b = Math.random() == 0.4
var x = "foo"
function func(x) {
	TAJS_dumpValue(x)
}
if (b)
	sd = eval;
else	
	sd = func
sd("x = 12")
TAJS_dumpValue(x);