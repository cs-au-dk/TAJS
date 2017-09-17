var x = {a:1,b:true}

TAJS_dumpObject(x);
TAJS_dumpAttributes(x, "a");
TAJS_dumpAttributes(x, "nosuchattribute");
TAJS_dumpAttributes(this, "NaN");
TAJS_dumpAttributes(Object, "prototype");

TAJS_dumpAttributes(this, "Function");
function f() {} // this line shouldn't affect the last TAJS_dumpAttributes
