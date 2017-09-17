var fun = function() {
    toString["foo" + ""] = 42;
};
fun();

TAJS_dumpValue(toString.foo);
TAJS_assert(toString.foo != undefined);
