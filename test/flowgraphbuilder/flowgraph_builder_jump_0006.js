l2: {
    l1: {
        try {
            throw "x";
        } catch(e){
            break l1;
        }finally{
            break l2;
        }
    }
    TAJS_assert(false);
}


TAJS_dumpValue('OK');
