var $ = {
    each: function (a, f) {
        for (var i = 0; i < a.length; i++) {
            f(i, a[i]);
        }
    }
};

var o = {};
$.each(['x', 'y'], function (i, e) {
    o[e] = function (p) {
        return e + p;
    }
});


TAJS_assert(o.x('x') === 'xx');
TAJS_assert(o.x('xx') === 'xxx');
TAJS_assert(o.x('y') === 'xy');
TAJS_assert(o.y('y') === 'yy');
TAJS_assert(o.y('yy') === 'yyy');
TAJS_assert(o.y('x') === 'yx');