gt = "crazy"
for (x in {}) {
    gt = gt + x;
}
dumpValue(gt) // Should be "crazy".
g = {b:23}
for (x in {}) {
    g[x];
}
