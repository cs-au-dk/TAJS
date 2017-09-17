var p = true;
l1: {
    with({p: false}){
        break l1;
    }
}
TAJS_assert(p);

TAJS_dumpValue('OK');
