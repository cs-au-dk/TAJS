function A() {}
A.prototype.count = 0;
function B() {}
B.prototype = new A;

var x = new B;
var y = new B;
TAJS_assert(x.count == 0);
TAJS_assert(y.count == 0);
x.count++;
TAJS_assert(x.count == 1);
TAJS_assert(y.count == 0);