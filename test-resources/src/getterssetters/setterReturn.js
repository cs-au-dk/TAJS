var o = {};
o.__defineSetter__('p', function () {
    return 87;
});
var result = (o.p = 42);
TAJS_assert(42 === result);