var p = {gh: 32, gb: 10}

dumpObject(p);
delete p.gb;
dumpObject(p);

dumpObject(this);

dumpAttributes(this, "Function");
dumpValue(delete this.Function);
dumpObject(this)

dumpAttributes(Number, "length");
dumpValue(delete Number.length);
dumpObject(Number)

var arr = [0,1,1,2,3,5,8,13,"not a fib number",21,34,55,89];
var t = 5;
dumpObject(arr);
delete arr[t+3];
dumpObject(arr);

function id(x) {return x}
var s = id("a") + id("b");
dumpValue(s);
delete arr[s]
dumpObject(arr);
