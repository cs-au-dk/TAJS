x = 0;
function spy() {
    return this;
}
try {
    throw spy;
} catch (e) {
    spy().x = 1;
    TAJS_assert(x === 1);
}
TAJS_assert(x === 1);
