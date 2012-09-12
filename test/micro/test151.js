var x = {a:42}
var y = {}
var z;

var u = Math.random()

function f() {
	y[87] = x.a;
//	dumpObject(y)
	z = y[u]
	dumpValue(z)
}

f()

//dumpValue(y.b)
//dumpValue(z)

