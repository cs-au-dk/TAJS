function isObj(x) {
    return typeof x === "object";
}
var y = {};
var x = TAJS_join(y, "string");
if (isObj(x)) {
    TAJS_assertEquals(y, x);
} else {
    TAJS_assertEquals("string", x);
}