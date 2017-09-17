y = "5"
try {
	if (Math.random())
		throw "hep"
	else
		y = 5;
} catch (e) {
	if (Math.random())
		throw e
	else
		y = {};
} finally {
	TAJS_dumpValue(y)
	TAJS_dumpObject(y)
}
