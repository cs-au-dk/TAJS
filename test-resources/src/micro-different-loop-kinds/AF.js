/* For-in Loops - Abstract Case */
function f1() { return 1; }
function f2() { return 2; }
function f3() { return 3; }
function f4() { return 4; }

var b = !Date.now(); // non-deterministic boolean value.
var o1 = {};
var o2 = {};
if (b) {
    o1.x = f1;
    o1.y = f2;
    o1.z = f3;
} else {
    o1.z = f3;
    o1.y = f2;
    o1.x = f1;
}

var k = Date.now() + "h";
o1[k] = f4;

// when we assume that the analysis is path-insensitive,
// the order of the iterations for the object o1 cannot be specified

for (var v in o1) {
    o2[v] = o1[v];
}

var result = o2.x !== f2;
TAJS_assert(result);
// TAJS-determinacy: BoolTop
// LSA: BoolTop
// CompAbs: true