var p = true;
l1: {
    try{
        with({p: false}){
     
        }
    }finally{
       break l1;
    }
    TAJS_assert(false);
}
TAJS_assert(p);

TAJS_dumpValue('OK');
