//This file just tests if the flowgraph -> js expression works.
function as() {
	
}
var o = {p: function() {return {}}}
var y = 3;

TAJS_dumpExp(1+y+3);
TAJS_dumpExp(y)
TAJS_dumpExp(4)
TAJS_dumpExp(as())
TAJS_dumpExp("(" + as() + (y + 2) + ")")
TAJS_dumpExp(2+3*y)
TAJS_dumpExp((2+y)*y)
try {throw 4} catch(e) {TAJS_dumpExp(e)}
TAJS_dumpExp(y/(1+y) + as())
TAJS_dumpExp(o.p)
TAJS_dumpExp(o[4])
TAJS_dumpExp(o[as()])
TAJS_dumpExp(o.p())
TAJS_dumpExp(o.p()[y])