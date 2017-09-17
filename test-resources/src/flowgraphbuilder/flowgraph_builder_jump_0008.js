var p = true;
l1: {
    try{
        with({p: false}){
     
        }
    }finally{

    }
}
TAJS_assert(p);

TAJS_dumpValue('OK');
