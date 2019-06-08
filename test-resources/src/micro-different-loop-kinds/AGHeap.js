/* General Loops - Abstract Case - Free variable */
function f1() { return 1; }
function f2() { return 2; }
function f3() { return 3; }

var b = !Date.now(); // non-deterministic boolean value.
var o1 = {x: f1, y: f2, z: f3};
var o2 = {};

var arr = [];
if (b) {
    arr.push("x");
    arr.push("y");
} else {
    arr.push("z");
    arr.push("y");
    arr.push("x");
}

// the array ’arr’ is not concrete when we assume the
// path-insensitive analysis.
var i = arr.length;

while (i--) { // the analysis cannot enumerate all the concrete iterations.
    var t = arr[i];
    var v = o1[t];
    o2[t] = (function(v) {
        return function() {
                return v;
            };
    }(v));
}
var result = o2.x() !== f2;
TAJS_assert(result);
// TAJS-determinacy: BoolTop
// LSA: BoolTop
// CompAbs: true