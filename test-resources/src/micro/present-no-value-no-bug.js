function pick(a) {
  return a.p.q;
}
function mk() {
  return {};
}
function f() {
  var a = mk();
  mk();
  pick(a);
}
function g() {
  pick();
}

var x = mk();
x.p = { q: 0 };

f();
g();