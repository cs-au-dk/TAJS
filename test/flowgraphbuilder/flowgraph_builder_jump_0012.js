var p = true;
l1: {
    try{
        with({p: false}){
            throw "x";
        }
    }catch(e){
       break l1;
    }finally{

    }
    TAJS_assert(false);
}
TAJS_assert(p);

TAJS_dumpValue('OK');
