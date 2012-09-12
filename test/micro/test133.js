var x;
var y;
function f1() {f2()}
function f2() {f3()}
function f3() {x = {a:42}; f4()}
function f4() {f5()}
function f5() {x.b = "foo"; f6()}
function f6() {f7()}
function f7() {y = x.b; f8()}
function f8() {}
f1();
assert(y == "foo");
 