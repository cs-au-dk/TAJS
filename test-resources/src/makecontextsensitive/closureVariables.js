TAJS_makeContextSensitive(f, 0);
function f(a) {
    function fa() {
        return a + 0
    };
    TAJS_makeContextSensitive(fa, -2);
    return fa;
}
var f1 = f(1);
var f2 = f(2);
var v1_c = f1();
var v2_c = f2();
var v3_c = v1_c + v2_c;
TAJS_assert(v3_c, "isMaybeSingleNum", true);

TAJS_makeContextSensitive(g, 0);
function g(a) {
    return function () {
        return a + 0
    };
}
var g1 = g(1);
var g2 = g(2);
var v1g = g1();
var v2g = g2();
var v3g = v1g + v2g;
TAJS_assert(v3g, "isMaybeSingleNum", false);

function h(a) {
    return function () {
        return a + 0
    };
}
var h1 = h(1);
var h2 = h(2);
var v1h = h1();
var v2h = h2();
var v3h = v1h + v2h;
TAJS_assert(v3h, "isMaybeSingleNum", false);
