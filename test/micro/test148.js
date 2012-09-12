var x = {a:42}
var y;
function f() {
  y = x.a;
}
f()
dumpValue(x.a)
x = {a:"foo"}
f()
dumpValue(x.a)
