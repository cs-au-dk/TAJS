function X() {
    var localThis = this;
    var globalThis = window;
    TAJS_assert(localThis === this);
    eval("TAJS_assert(localThis === this)");
    setInterval("TAJS_assert(globalThis === this)", 10);
    TAJS_assert(localThis === this);
}

new X();
