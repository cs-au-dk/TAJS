var x0 = Infinity;
var x1 = isNaN (Infinity) + " " + isNaN(0/0);
var x2 = Math.E;
var NaN = 0/0;
var x5 = Math.hasOwnProperty();
var x5str = Math.toString();
var x6 = parseInt("  4711   ");
var x7 = NaN;

var y1 = Math.max(42, 77, -20, 99, -1, 111);

var ns1 = new String();

var zz = "fooooooo";

TAJS_dumpValue(x0);
TAJS_dumpValue(x1);
TAJS_dumpValue(x2);
TAJS_dumpValue(NaN);
TAJS_dumpValue(x5);
TAJS_dumpValue(x5str);
TAJS_dumpValue(x6);
TAJS_dumpValue(x7);

TAJS_dumpValue(y1);

TAJS_dumpValue(ns1);
