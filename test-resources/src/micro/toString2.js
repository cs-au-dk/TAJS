var x1 = {toString: function() {return "1";}}
var x2 = {valueOf: function() {return "2";}}
var x3 = {toString: function() {return 3;}}
var x4 = {valueOf: function() {return 4;}}
var x5 = {}
var x6 = {toString: function() {return 5;}, valueOf: function() {return 6;}}

var p1 = x1 + 42;
var m1 = x1 - 42;

var p2 = x2 + 42;
var m2 = x2 - 42;

var p3 = x3 + 42;
var m3 = x3 - 42;

var p4 = x4 + 42;
var m4 = x4 - 42;

var p5 = 42 + x1;
var m5 = 42 - x1;

var p6 = 42 + x2;
var m6 = 42 - x2;

var p7 = 42 + x3;
var m7 = 42 - x3;

var p8 = 42 + x4;
var m8 = 42 - x4;

var b = Math.random();

var p9 = (b ? x1 : x3) + 42;
var m9 = 100 + (b ? x1 : x3) - 42;

var t10 = "a" + x5;
var t11 = 1000 + x5;

var t12 = "a" + x6;
var t13 = 1000 + x6;

var t14 = x5.valueOf();
var t15 = x5.toString();

var t16 = "a" + x2;
var t17 = 1000 + x2;

TAJS_assert(p1 === "142");
TAJS_assert(m1 === -41);
TAJS_assert(p2 === "242");
TAJS_assert(m2 === -40);
TAJS_assert(p3 === 45);
TAJS_assert(m3 === -39);
TAJS_assert(p4 === 46);
TAJS_assert(m4 === -38);
TAJS_assert(p5 === "421");
TAJS_assert(m5 === 41);
TAJS_assert(p6 === "422");
TAJS_assert(m6 === 40);
TAJS_assert(p7 === 45);
TAJS_assert(m7 === 39);
TAJS_assert(p8 === 46);
TAJS_assert(m8 === 38);
TAJS_assert(p9, 'isMaybeSingleStr || isMaybeNumUInt', true); //TAJS_assert(p9 === "142" || p9 === 45);
TAJS_assert(m9, 'isMaybeNumUInt', true); //TAJS_assert(m9 === (100 + -41) || m9 === (100 + -39));
TAJS_assert(t10 === "a[object Object]");
TAJS_assert(t11 === "1000[object Object]");
TAJS_assert(t12 === "a6");
TAJS_assert(t13 === 1006);
TAJS_assert(t14 === x5);
TAJS_assert(t15 === "[object Object]");
TAJS_assert(t16 === "a2");
TAJS_assert(t17 === "10002");

x5[x6] = "foo";
TAJS_assert(x5[5] === "foo");
