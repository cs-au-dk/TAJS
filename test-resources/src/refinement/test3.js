var apply = function(f,a) {
  f(a);
}

var f1 = function(s) {
  TAJS_assert(s === "str");
}
var f2 = function(b) {
  TAJS_assert(b === false);
}

apply(f1, "str");
apply(f2, false);
