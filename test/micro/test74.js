var gt = "crazy"
for (x in {}) {
    gt = gt +x;
}
TAJS_dumpValue(gt) // Should be "crazy".
var g = {a: 234, b:23, t:gt}
var sum = 0;
for (x in g) {
    TAJS_dumpValue(x)
    sum = sum + g[x];
}
TAJS_dumpValue(sum);
