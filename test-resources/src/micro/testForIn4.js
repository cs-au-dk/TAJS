var x =  {
		  foo: function testForIn1() { return 7; },
		  bar: function testForIn2() { return "whatever"; }
		};

var z = {};
for(var y in x) {
  z[y] = (x[y])();
}
TAJS_dumpObject(z);