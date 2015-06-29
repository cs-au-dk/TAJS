function f(){
    try{
        p();
    }finally{
        try{
            p();
        } catch (e){
            p();
        }
    }
}
f();
TAJS_assert(false);
