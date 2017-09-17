TAJS_makeContextSensitive(f, 0, {caller: g});
function f(a){
    return a + 0;
}
function g() {
    var v1_c = f(1);
    var v2_c = f(2);
    var v3_c = v1_c + v2_c;
    TAJS_assert(v3_c, "isMaybeSingleNum", true);
}
var v1 = f(1);
var v2 = f(2);
var v3 = v1 + v2;
TAJS_assert(v3, "isMaybeSingleNum", false);
TAJS_assert(v3, "isMaybeNumUInt", true);

g();