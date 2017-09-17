g = function () {
    var o = {};
    o.p1 = {};
    this.f(o);
    var var0 = o.p2;
    TAJS_assert(var0 !== undefined);
}

g.prototype = {
    xxx14: 0,
    xxx9: 0,
    xxx5: 0,
    xxx6: 0,
    xxx7: 0,
    xxx8: 0,
    xxx0: 0,
    xxx1: 0,
    xxx2: 0,
    xxx3: 0,
    f: function (o) {
        var var1 = document.createElement('canvas'),
            var2 = o.p1.xxx15;
        var1.xxx10 = 0;
        o.xxx12 = 0;
        if (var2) {
        }
        o.p2 = var1.getContext();
    }
}
new g();