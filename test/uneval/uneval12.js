//This file just tests if the flowgraph -> js expression works.
function as() {
	
}
var o = {p: function() {return {}}}
var y = 3;

dumpExp(1+y+3);
dumpExp(y)
dumpExp(4)
dumpExp(as())
dumpExp("(" + as() + (y + 2) + ")")
dumpExp(2+3*y)
dumpExp((2+y)*y)
try {throw 4} catch(e) {dumpExp(e)}
dumpExp(y/(1+y) + as())
dumpExp(o.p)
dumpExp(o[4])
dumpExp(o[as()])
dumpExp(o.p())
dumpExp(o.p()[y])