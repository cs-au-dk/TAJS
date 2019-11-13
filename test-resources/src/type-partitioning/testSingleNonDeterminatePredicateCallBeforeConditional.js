function isObj(x) {
    return typeof x === "object";
}
var y = TAJS_make("AnyBool") ? {} : {};
var x = TAJS_join(y, TAJS_make("AnyStr"));
var isObject = isObj(x);
if (isObject) {
    TAJS_assertEquals(y, x);
} else {
    TAJS_assertEquals(TAJS_make("AnyStr"), x);
}