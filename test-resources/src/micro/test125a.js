x = 0;
function spy() {
    return this;
}
try {
    throw spy
} catch (spy) {
    this.x = 1;
    TAJS_assert(x === 1);
}

TAJS_assert(x === 1);