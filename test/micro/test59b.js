function f(x) {
	if (x)
	  return "a";
	else
	  return "b";
}

var a = f(true) + f(false);

var x = {};

x[a] = "hello";

dumpObject(x);
