var x = {foo: 1, bar: 2};

var y, z;

for (var p in x) {
    if (p == "foo") {
        var t = {a: x[p]};
        y = t;
    } else {
        var t = {a: x[p]};
        z = t;
    }
}

TAJS_dumpValue(y);
TAJS_dumpValue(z);
TAJS_dumpObject(y);
TAJS_dumpObject(z);

z.a = "dyt";

TAJS_dumpValue(y.a);
TAJS_dumpValue(z.a);

TAJS_assert(y.a === 1);
TAJS_assert(z.a, 'isMaybeSingleNum||isMaybeSingleStr');
