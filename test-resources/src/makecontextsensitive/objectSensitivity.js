TAJS_makeContextSensitive(f_c, -1);
function f_c() {
    return this + 0;
}
var v1_c = f_c.call(1);
var v2_c = f_c.call(2);
var v3_c = v1_c + v2_c;
TAJS_assert(v3_c, "isMaybeSingleNum", true);


TAJS_makeContextSensitive(f, 42 /* disables the default object sensitivity */);
function f() {
    return this + 0;
}
var v1 = f.call(1);
var v2 = f.call(2);
var v3 = v1 + v2;
TAJS_assert(v3, "isMaybeSingleNum", false);