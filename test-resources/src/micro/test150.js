var x;
var y;

var f = function() {
  x = y;	
//  TAJS_dumpValue(x);
}

var g = function() {
	  f();
	//  TAJS_dumpValue(x);
	}

var h = function() {
	  g();
	//  TAJS_dumpValue(x);
	}

x = 42;
y = "foo";
h();
TAJS_dumpValue(x);
