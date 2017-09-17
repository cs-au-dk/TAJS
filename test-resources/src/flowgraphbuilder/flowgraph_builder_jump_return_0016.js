function f(){
    try{
        try{
            return toString() && false;
        } finally{
            return toString() && false;
        }  
    } finally {
        return toString() && true;
    }
}
TAJS_assert(f());

TAJS_dumpValue('OK');
