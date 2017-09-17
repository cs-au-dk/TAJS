function f(){
    try{
        return;
    }finally{
        try{
        } finally {
        }
    }
}
f();
TAJS_dumpValue("OK");
