var src = {x:1,y:2};
var dst = {};

for (var p in src) {
    dst[p] = src[p];
    TAJS_dumpValue(p);
}

TAJS_dumpValue(p);
TAJS_dumpValue(dst.x)
TAJS_dumpValue(dst.y)
TAJS_dumpValue(dst.z)
TAJS_dumpObject(dst);
