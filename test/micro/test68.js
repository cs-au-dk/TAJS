var x = {a:1,b:true}

dumpObject(x);
dumpAttributes(x, "a");
dumpAttributes(x, "nosuchattribute");
dumpAttributes(this, "NaN");
dumpAttributes(Object, "prototype");

dumpAttributes(this, "Function");
function f() {} // this line shouldn't affect the last dumpAttributes
