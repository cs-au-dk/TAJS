function f1() { return 1; }
function f2() { return 2; }
function f3() { return 3; }

var str = Math.random() ? "x" : Math.random() ? "y" : "z"

var o1 = {x: f1, y: f2, z: f3};
var o2 = {};

var v = o1[str]
o2[str] = v;

var res = o2.x !== f2;
TAJS_assert(res);
// TAJS-determinacy: BoolTop
// LSA: BoolTop
// CompAbs: BoolTop
