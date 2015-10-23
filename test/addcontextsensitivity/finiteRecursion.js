function f(a){
    TAJS_addContextSensitivity("a");
    if(a == 10)
        return a;
    return f(a + 1);
}
var v0 = f(0);
TAJS_assert(v0, 'isMaybeSingleNum');
var v1 = f(1);
TAJS_assert(v1, 'isMaybeSingleNum');
var v2 = f(2);
TAJS_assert(v2, 'isMaybeSingleNum');
