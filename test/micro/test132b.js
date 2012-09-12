function A() {}
A.prototype.count = 42;
function B() {}
B.prototype = new A;

var x = new B;
var y = x.count;
