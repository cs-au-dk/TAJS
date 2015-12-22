var t = {};

var x1 = {valueOf: {}};
try {
  t[x1] = true;
} catch (e) {
  TAJS_assert(false);
}
TAJS_dumpObject(t); // expected: {"[object Object]":true}

var x2 = {toString: {}};
try {
  t[x2] = true;
  TAJS_dumpState();
} catch (e) {
  TAJS_dumpObject(t);
}
TAJS_dumpObject(t);

var x3 = {valueOf: {}};
try {
  TAJS_dumpValue(123 - x3); // expected: should give NaN
  //TAJS_assert(false);
} catch (e) {
  TAJS_dumpValue("should not be reached"); // expected: not reached
}
TAJS_dumpValue("reached");

var x4 = {toString: {}};
try {
  TAJS_dumpValue(123 - x4); // expected: TypeError
  TAJS_assert(false);
} catch (e) {
  TAJS_dumpValue("type error");
}
TAJS_dumpValue("reached");