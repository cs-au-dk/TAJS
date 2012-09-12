var f = new Function ("a", "return a;");
function g (x) { return x; }
var h = new Function ("Lblablabla");
var i = new Function ("x", "y", "return x+y;");

dumpValue(f.prototype);
dumpValue(g.prototype);
dumpValue(h.prototype);
dumpValue(i.valueOf());

dumpValue(Function.prototype);
dumpValue(Function.prototype());

