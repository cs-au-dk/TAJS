var x = 0;
function f() {x; return {};}
v = f();
x = 1;
if (Math.random()) x = v;
f();
x = 2;
f();
TAJS_dumpValue(x);
