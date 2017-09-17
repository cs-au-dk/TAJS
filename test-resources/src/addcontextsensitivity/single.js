TAJS_addContextSensitivity(f_c, "a");
function f_c(a){
    return a + 0;
}
var v1_c = f_c(1);
var v2_c= f_c(2);
var v3_c = v1_c + v2_c;
TAJS_assert(v3_c, "isMaybeSingleNum", true);

function f(a){
    return a + 0; // need to resolve here, otherwise TAJS is too smart :)
}
var v1 = f(1);
var v2 = f(2);
var v3 = v1 + v2;
TAJS_assert(v3, "isMaybeSingleNum", false);
TAJS_assert(v3, "isMaybeNumUInt", true);
