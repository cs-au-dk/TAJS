var id = function(x) {
  TAJS_dumpValue(x);
  return x;
}

var a = id(42);
var b = id(false);

TAJS_assert(b === false); // trigger refinement if assertion may fail according to the forward analysis
