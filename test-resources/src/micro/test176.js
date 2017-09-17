g = {a: 234, b:23, t:44}
o = {}
TAJS_dumpObject(g)
for (var x in g) {
    o[x] = g[x]
}
TAJS_dumpObject(o)
