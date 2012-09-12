var x;
var y;

var f = function() {
  x = y;	
//  dumpValue(x);
}

var g = function() {
	  f();
	//  dumpValue(x);
	}

var h = function() {
	  g();
	//  dumpValue(x);
	}

x = 42;
y = "foo";
h();
dumpValue(x);
