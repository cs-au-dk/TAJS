function F(a) {
  this.foo = a;
}

var x = new F(117)
var y = new F(87);

TAJS_dumpValue(x.foo);
TAJS_dumpValue(y.foo);
