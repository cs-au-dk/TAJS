function f(x) {
        x = x * -1
	return arguments[0]
}

TAJS_dumpValue(f(3))

function g(x) {
        x = x * -1
	return x
}

TAJS_dumpValue(g(4))
