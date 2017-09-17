function f(){
    try{
        with({p: false}){
            v0 = p;
            return;
        }
    }finally{
        v1 = v0 === false;
        try{
            p;
        } catch (e){
            v2 = true;
        }
    }
}
f();
TAJS_assert(v1);
TAJS_assert(v2);

TAJS_dumpValue('OK');
