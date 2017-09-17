TAJS_makeContextSensitive(f_c12, 0);
TAJS_makeContextSensitive(f_c34, 0);
TAJS_makeContextSensitive(f_c56, 0);
// vary both
function f_c12(a,b){
    return a+b;
}
var v1_c = f_c12(1,2);
var v2_c= f_c12(3,4);
var v12_c = v1_c + v2_c;
assertSingleNum(v12_c);

// vary sensitive
function f_c56(a,b){
    return a+b;
}
var v5_c = f_c56(1,2);
var v6_c= f_c56(3,2);
var v56_c = v5_c + v6_c;
assertSingleNum(v56_c);

// vary non-sensitive
function f_c34(a,b){
    return a+b;
}
var v3_c = f_c34(1,2);
TAJS_assert(v3_c, "isMaybeNumUInt", false);
var v4_c= f_c34(1,4);
TAJS_assert(v4_c, "isMaybeNumUInt", true);
