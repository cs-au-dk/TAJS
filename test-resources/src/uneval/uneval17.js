var horse = "horse_var"
var cow = "cow_var"

function f(x) {
	return eval(x);
}

TAJS_dumpValue(f("horse"))
TAJS_dumpValue(f("cow"))