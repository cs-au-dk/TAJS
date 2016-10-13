try {
  var x = {
    get f(v) { return 1; }, // wrong number of arguments, gives SyntaxError exception in V8 and Edge but should be parse error according to ECMAScript spec!?! - and crashes our parser :-(
  }
  TAJS_dumpValue("expect SyntaxError")
} catch (e) {
  TAJS_dumpValue(e)
}

try {
  var x = {
    set f() { return 1; }, // wrong number of arguments, give SyntaxError exception in V8 and Edge but accepted by ECMAScript spec?!? - and crashes our parser :-(
  }
  TAJS_dumpValue("expect SyntaxError")
} catch (e) {
  TAJS_dumpValue(e)
}
