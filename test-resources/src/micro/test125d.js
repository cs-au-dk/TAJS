function getReceiver() {
    return this;
}
try {
    throw getReceiver;
} catch (getReceiver_thrown) {
    var rec1 = getReceiver();
    var rec2 = getReceiver_thrown();
    TAJS_dumpValue(rec1);
    TAJS_dumpValue(rec2);
    TAJS_assert(rec1 === rec2);
}
