var x1 = {toString: function() {if (Math.random()) throw 42; else return "dyt";}}
var x5 = {}

try {
  x5[x1] = "foo";
} catch (e) {
  TAJS_dumpValue(e);
}
TAJS_dumpValue(x5.dyt);

var x2 = {}
if (Math.random())
    x2.toString = function() {return "foo";};
else
    x2.toString = function() {return 222;};
x5[x2] = true;
TAJS_dumpValue(x5.foo);
TAJS_dumpValue(x5[222]);

var q = x5[x2];
