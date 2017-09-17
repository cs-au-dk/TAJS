var foo = {bar: 123,
set qqq(value) {
                         TAJS_dumpValue("HERE3");
                         q = value;
                     }
}
var p = "qqq"
var q = "QWERTY";
foo.__defineGetter__(p, function () { // commenting out the next 6 lines make it work...
    return 42;
});
foo.__defineSetter__(p, function (value) {
    TAJS_dumpValue("HERE");
    q = value;
});

TAJS_dumpObject(foo)

TAJS_dumpValue(foo.bar); // should be 123
TAJS_assert(foo.bar === 123);
foo.qqq = true;
TAJS_dumpValue(q); // should be true!!!
TAJS_assert(q);
