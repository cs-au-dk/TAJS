var src = {x:1,y:2};
var dst = {};

for (var p in src) {
    dst[p] = src[p];
    dumpValue(p);
}

dumpValue(p);
dumpValue(dst.x)
dumpValue(dst.y)
dumpValue(dst.z)
dumpObject(dst);
