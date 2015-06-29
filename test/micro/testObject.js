var t1 = Object();
var t2 = Object(null);
var t3 = Object(undefined);
var t4 = Object(1, 2);
var t5 = Object("foo");

TAJS_dumpObject(t1);
TAJS_dumpObject(t2);
TAJS_dumpObject(t3);
TAJS_dumpObject(t4);
TAJS_dumpObject(t5);

TAJS_dumpValue(t1);
TAJS_dumpValue(t2);
TAJS_dumpValue(t3);
TAJS_dumpValue(t4);
TAJS_dumpValue(t5);



var t6 = new Object();
var t7 = new Object(null);
var t8 = new Object(undefined);
var t9 = new Object(1, 2);
var t10 = new Object("foo");

TAJS_dumpObject(t6);
TAJS_dumpObject(t7);
TAJS_dumpObject(t8);
TAJS_dumpObject(t9);
TAJS_dumpObject(t10);

TAJS_dumpValue(t6);
TAJS_dumpValue(t7);
TAJS_dumpValue(t8);
TAJS_dumpValue(t9);
TAJS_dumpValue(t10);

var x1 = t8.toString();
var x2 = t9.toString();
var x3 = t10.toString();

var x4 = t8.valueOf();
var x5 = t9.valueOf();
var x6 = t10.valueOf();

TAJS_dumpValue(x1);
TAJS_dumpValue(x2);
TAJS_dumpValue(x3);
TAJS_dumpValue(x4);
TAJS_dumpValue(x5);
TAJS_dumpValue(x6);

