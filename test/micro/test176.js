g = {a: 234, b:23, t:44}
o = {}
dumpObject(g)
for (x in g) {
    o[x] = g[x]
}
dumpObject(o)
