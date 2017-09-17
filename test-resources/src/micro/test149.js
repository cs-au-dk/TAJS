var t;
var x;

var f = function() {
   t.next = x;	
}

x = null;
t = {}; // 14
f();
x = t;

t = {}; // 20
f();
x = t;

t = {}; // 26
f();
x = t;

t = {}; // 32
f();
x = t;

t = {}; // 38
f();
x = t;
