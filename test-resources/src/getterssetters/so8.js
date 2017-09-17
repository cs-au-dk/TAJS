var foo = {bar: 123};
Object.defineProperty(foo, 'bar', {
    get: function () {
        return this.bar;
    },
    set: function (value) {
        this.bar = value;
    }
});
foo.bar = 456;
TAJS_assert(false);
