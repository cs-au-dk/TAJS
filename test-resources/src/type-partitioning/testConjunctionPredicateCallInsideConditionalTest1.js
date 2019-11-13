function isObj(x) {
    return typeof x === "object";
}
function isString(x) {
    return typeof x === "string";
}
var y = {};
var res = TAJS_join(y, "string", 5);
var x = res;

if (isObj(x) && isString(x)) {
    TAJS_assert(false);
} else {
    if (x === y) {
        TAJS_assert(true);
    }
    if (x === "string") {
        TAJS_assert(true);
    }
    if (x === 5) {
        TAJS_assert(true);
    }
}