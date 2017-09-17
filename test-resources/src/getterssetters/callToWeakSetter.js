var set_triggered = false;
var o = {};
if (Math.random()) {
    o.__defineSetter__('p', function () {
        set_triggered = true;
    });
}
o.p = 42;
TAJS_assert(o.p, 'isMaybeUndef');
TAJS_assert(set_triggered, 'isMaybeAnyBool');