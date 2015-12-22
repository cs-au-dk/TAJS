var x = {a:42}
var y;
function f() {
  y = x.a;
}
f()
TAJS_dumpValue(x.a)
x = {a:"foo"}
f()
TAJS_dumpValue(x.a)
