function f(){
    try{
      return false;  
    } finally{
      return true;  
    }
}
TAJS_assert(f());

TAJS_dumpValue('OK');
