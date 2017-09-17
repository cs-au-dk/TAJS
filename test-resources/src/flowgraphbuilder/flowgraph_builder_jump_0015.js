var p = true;
with({p: false}){
    
}
TAJS_assert(p);

TAJS_dumpValue('OK');
