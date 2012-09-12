function F(a) {
  this.foo = a;
}

var x = new F(117)
var y = new F(87);

dumpValue(x.foo);
dumpValue(y.foo);
