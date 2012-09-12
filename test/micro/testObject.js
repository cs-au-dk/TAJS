var t1 = Object();
var t2 = Object(null);
var t3 = Object(undefined);
var t4 = Object(1, 2);
var t5 = Object("foo");

dumpObject(t1);
dumpObject(t2);
dumpObject(t3);
dumpObject(t4);
dumpObject(t5);

dumpValue(t1);
dumpValue(t2);
dumpValue(t3);
dumpValue(t4);
dumpValue(t5);



var t6 = new Object();
var t7 = new Object(null);
var t8 = new Object(undefined);
var t9 = new Object(1, 2);
var t10 = new Object("foo");

dumpObject(t6);
dumpObject(t7);
dumpObject(t8);
dumpObject(t9);
dumpObject(t10);

dumpValue(t6);
dumpValue(t7);
dumpValue(t8);
dumpValue(t9);
dumpValue(t10);

var x1 = t8.toString();
var x2 = t9.toString();
var x3 = t10.toString();

var x4 = t8.valueOf();
var x5 = t9.valueOf();
var x6 = t10.valueOf();

dumpValue(x1);
dumpValue(x2);
dumpValue(x3);
dumpValue(x4);
dumpValue(x5);
dumpValue(x6);

