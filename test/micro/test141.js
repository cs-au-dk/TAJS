var C = function(v) {
  this.inc = function() {return ++v}
}

var x = new C(7);

TAJS_dumpValue(x.inc());
TAJS_dumpState();