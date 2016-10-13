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
function dummy(){}
mk().p = { q: 0 };
dummy();

f();
g();