var foo = {};
var UInt = Math.random()? 3: 4;
var p = "foo" + UInt + "bar";
var x = true;
Object.defineProperty(foo, p, {
    get: function () {
        return false;
    },
    set: function (value) {
        x = value;
    }
});
var t1 = foo.foo123bar // either false (if p == 123) or undefined (otherwise)
TAJS_dumpValue(t1);
TAJS_assert(t1, "isMaybeUndef || isMaybeFalseButNotTrue");

foo.foo123bar = p; // writes to x or to foo.foo123bar
TAJS_dumpValue(x)
TAJS_assert(x, "isMaybeStrPrefixedIdentifierParts || isMaybeAnyBool")

var t3 = foo.foo123bar; // either false or p
TAJS_dumpValue(t3)
TAJS_assert(t3, "isMaybeUndef || isMaybeFalseButNotTrue || isMaybeAnyStr")
