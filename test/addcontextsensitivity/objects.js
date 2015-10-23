function f_c(a, b){
    TAJS_addContextSensitivity("a");
    return b;
}
var v1_c = f_c({}, 1);
TAJS_assert(v1_c, "isMaybeNumUInt", false);
var v2_c= f_c({}, 2);
TAJS_assert(v2_c, "isMaybeNumUInt", false);
var v3_c = v1_c + v2_c;
TAJS_assert(v2_c, "isMaybeNumUInt", false);



function g_c(a, b){
    TAJS_addContextSensitivity("a");
    return b;
}
var o = {};
var w1_c = g_c(o, 1);
TAJS_assert(w1_c, "isMaybeNumUInt", false);
var w2_c= g_c(o, 2);
TAJS_assert(w2_c, "isMaybeNumUInt", true);