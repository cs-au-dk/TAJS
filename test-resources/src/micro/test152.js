function f1() {f2();}
function f2() {f3();}
function f3() {f4();TAJS_dumpState();}
function f4() {x=y;}

var x = 1, y = 2;
f1();
TAJS_dumpValue(x);