function f(){
    try{
        try{
            return false;  
        } finally{
            return false;  
        }  
    } finally {
        return true;
    }
}
TAJS_assert(f());

TAJS_dumpValue('OK');
