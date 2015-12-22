function f(x) {
  var x;
  return x;
}
TAJS_dumpValue(f(7)); // 7.0

function g(x) {
  function x() {};
  return x;
}
TAJS_dumpValue(g(7)); // {@x#fun3}
