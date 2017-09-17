var f = new Function ("a", "return a;");
function g (x) { return x; }
var h = new Function ("Lblablabla");
var i = new Function ("x", "y", "return x+y;");

TAJS_dumpValue(f.prototype);
TAJS_dumpValue(g.prototype);
TAJS_dumpValue(h.prototype);
TAJS_dumpValue(i.valueOf());

TAJS_dumpValue(Function.prototype);
TAJS_dumpValue(Function.prototype());

