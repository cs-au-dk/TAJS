function p(){}
function f(){
    try{
        
    }finally{
        try{
            p();
        } catch (e){
            p();
        }
    }
}
f();
TAJS_dumpValue("OK");
