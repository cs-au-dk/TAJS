var foo = {barr: 123,
  get bar() {
    TAJS_dumpValue(this)
    return this.baz;
  },
  get baz() {
    TAJS_dumpValue(this)
    return this.barr;
  }
};
var barr = "WRONG";
var gaz = foo.bar;
TAJS_dumpValue(gaz);
TAJS_assert(gaz === 123);
