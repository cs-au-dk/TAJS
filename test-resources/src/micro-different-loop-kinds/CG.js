/* General Loops - Concrete Case */
function f1() { return 1; }
function f2() { return 2; }
function f3() { return 3; }

var o1 = {x: f1, y: f2, z: f3};
var o2 = {};
var arr = ["x", "y"];

// the array ’arr’ is concrete
var i = arr.length;

while (i--) { // the analysis can enumerate all the concrete iterations.
    var t = arr[i];
    o2[t] = o1[t];
}
var result = o2.x !== f2;
TAJS_assert(result);
// TAJS-determinacy: true
// LSA: true
// CompAbs: true