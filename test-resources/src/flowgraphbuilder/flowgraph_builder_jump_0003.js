l1: {
    try {
        throw "x";
    } catch(e){
        break l1;
    }finally{

    }
    TAJS_assert(false);
}


TAJS_dumpValue('OK');
