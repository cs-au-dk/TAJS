var foo = { }

TAJS_dumpObject(foo)

Object.defineProperty(foo, "qqq", {
    set: function (value) {
        TAJS_dumpValue("HERE2");
        q = value;
    }
});

TAJS_dumpObject(foo)

var q = "QWERTY";
foo.qqq = true;
TAJS_dumpValue(q); // should be true!!!
TAJS_assert(q);
