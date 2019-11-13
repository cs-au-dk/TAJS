function isObj(x) {
    return typeof x === "object";
}
function isString(x) {
    return typeof x === "string";
}
var y = {};
var res = TAJS_join(y, "string", 5);
var x = res;

if (isString(x) || isObj(x)) {
    if (x === y) {
        TAJS_assert(true);
    }
    if (x === "string") {
        TAJS_assert(true);
    }
    TAJS_assertEquals(5, x, false);
} else {
    TAJS_assertEquals(5, x);
}