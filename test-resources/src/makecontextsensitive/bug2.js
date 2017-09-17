TAJS_makeContextSensitive(f, 0);

var CONSTANT = 1;

// when only varying the non-sensitive parameter, the
// context sensitivity should _not_ be toggled.
function f(a,b){
    return a+b;
}

var f_c1 = f(CONSTANT,2);
TAJS_assert(f_c1, "isMaybeNumUInt", false);
var f_c2 = f(CONSTANT,3);

var f_c3 = f(CONSTANT,4);
TAJS_assert(f_c3, "isMaybeNumUInt", true);
