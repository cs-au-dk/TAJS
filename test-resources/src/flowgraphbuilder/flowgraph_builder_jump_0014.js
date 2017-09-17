var p = true;
l1: {
    try{
        with({p: false}){
            throw "x";
        }
    }catch(e){
        throw "y";
    }finally{
        break l1;
    }
    TAJS_assert(false);
}
TAJS_assert(p);

TAJS_dumpValue('OK');
