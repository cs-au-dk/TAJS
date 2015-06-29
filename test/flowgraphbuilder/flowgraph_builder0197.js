var K = function () { this.p = 42;};
var k = new K;
TAJS_dumpValue(k.p);

var K_bug = (function () { this.p = 42;});
var k_bug = new K_bug
TAJS_dumpValue(k_bug.p);

