var C = function(v) {
  this.inc = function() {return ++v}
}

var x = new C(7);

dumpValue(x.inc());
dumpState();