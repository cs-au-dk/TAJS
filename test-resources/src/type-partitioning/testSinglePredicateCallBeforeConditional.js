function isObj(x) {
    return typeof x === "object";
}
var y = {};
var x = TAJS_join(y, "string");
var isObject = isObj(x);
if (isObject) {
    TAJS_assertEquals(y, x);
} else {
    TAJS_assertEquals("string", x);
}