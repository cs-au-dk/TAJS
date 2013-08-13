// for .. in test

var p = {gh: 32, gb: 10}
//var sum = 0;

for (var x in p) {
	dumpValue(x);
//    sum = sum + x;
}

dumpValue(x); // should be IdentStr.
