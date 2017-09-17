var o = {
    a: 7,
    get b() {
        return this.a + 1;
    },
    set c(x) {
        this.a = x / 2
    }
};

TAJS_assert(o.a === 7);
TAJS_assert(o.b === 8);
o.c = 50;
TAJS_assert(o.a === 25);
