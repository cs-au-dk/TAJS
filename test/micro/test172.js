function f(x, y) {
        y = x * -1
	return arguments[1]
}

dumpValue(f(3, 0))

function g(x, y) {
        x = x * -1
	return arguments[1]
}

dumpValue(g(4, 0))
