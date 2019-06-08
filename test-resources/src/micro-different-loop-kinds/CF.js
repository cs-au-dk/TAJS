/* For-in Loops - Concrete Case */
function f1() { }
function f2() { }
function f3() { }

var o1 = {x: f1, y: f2, z: f3};
var o2 = {};

// the order of the iterations for the object o1 can be specified.
for (var v in o1) {
    o2[v] = o1[v];
}

var result = o2.x !== f2;
TAJS_assert(result);
// TAJS-determinacy: true
// LSA: true
// CompAbs: true