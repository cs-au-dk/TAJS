var x0 = Infinity;
var x1 = isNaN (Infinity) + " " + isNaN(0/0);
var x2 = Math.E;
var NaN = 0/0;
var x5 = Math.hasOwnProperty();
var x5str = Math.toString();
var x6 = parseInt("  4711   ");
var x7 = this.NaN;

var y1 = Math.max(42, 77, -20, 99, -1, 111);

var ns1 = new String();

var zz = "fooooooo";

dumpValue(x0);
dumpValue(x1);
dumpValue(x2);
dumpValue(NaN);
dumpValue(x5);
dumpValue(x5str);
dumpValue(x6);
dumpValue(x7);

dumpValue(y1);

dumpValue(ns1);
