function f(x) {
        x = x * -1
	return arguments[0]
}

dumpValue(f(3))

function g(x) {
        x = x * -1
	return x
}

dumpValue(g(4))
