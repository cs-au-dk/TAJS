var p = {gh: 32, gb: 10}

TAJS_dumpObject(p);
delete p.gb;
TAJS_dumpObject(p);

TAJS_dumpObject(this);

TAJS_dumpAttributes(this, "Function");
TAJS_dumpValue(delete this.Function);
TAJS_dumpObject(this)

TAJS_dumpAttributes(Number, "length");
TAJS_dumpValue(delete Number.length);
TAJS_dumpObject(Number)

var arr = [0,1,1,2,3,5,8,13,"not a fib number",21,34,55,89];
var t = 5;
TAJS_dumpObject(arr);
delete arr[t+3];
TAJS_dumpObject(arr);

function id(x) {return x}
var s = id("a") + id("b");
TAJS_dumpValue(s);
delete arr[s]
TAJS_dumpObject(arr);
