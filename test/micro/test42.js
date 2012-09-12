var f;
var a = 42;
a = a + 1;
if (Math.random())
  f = 1;//function() {return 7;}
else if (Math.random())
  f = "sdf";
else
  f = {};
dumpValue(f);
var x = f();
dumpValue(x);
