function f(){
    with({p: false}){
        v = p;
        return;
    }
}
f();
TAJS_assert(v === false);
TAJS_assert(typeof p === "undefined");

TAJS_dumpValue('OK');
