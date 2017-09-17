var o = {};
if (Math.random()) {
    o.__defineGetter__('p', function () {
        this.p; // infinite recursion
    });
}

var v = o.p; // triggers infinite recursion OR results in absent -> undefined
TAJS_assert(v === undefined);
