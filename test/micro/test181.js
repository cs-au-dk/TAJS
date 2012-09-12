function f(x) {
  var x;
  return x;
}
dumpValue(f(7)); // 7.0

function g(x) {
  function x() {};
  return x;
}
dumpValue(g(7)); // {@x#fun3}
