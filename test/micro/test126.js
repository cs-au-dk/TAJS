function fail(x) {
    throw x;
}
    g = {gt: "funny", b: 24, c:34}

try {
    with(g) {
	fail(gt)
    }
} catch (e) {
    TAJS_dumpValue(e)
    TAJS_dumpValue(gt) //Should be absent
}
