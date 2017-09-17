var v;
function f(){
    try{
        return;
    }finally{
        try{
        } catch (e){
        }
    }
}
f();
TAJS_dumpValue("OK");