var x = {a:42}
var y = {}
var z;

var u = Math.random()

function f() {
	y[87] = x.a;
//	TAJS_dumpObject(y)
	z = y[u]
	TAJS_dumpValue(z)
}

f()

//TAJS_dumpValue(y.b)
//TAJS_dumpValue(z)

