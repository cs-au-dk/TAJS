function f(x, y) {
        y = x * -1
	return arguments[1]
}

TAJS_dumpValue(f(3, 0))

function g(x, y) {
        x = x * -1
	return arguments[1]
}

TAJS_dumpValue(g(4, 0))
