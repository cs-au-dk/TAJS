var horse = "horse_var"
var cow = "cow_var"

function f(x) {
	return eval(x);
}

dumpValue(f("horse"))
dumpValue(f("cow"))