function f(){
    try{
        return;
    }finally{
        v = true;;
    }
}
f();
TAJS_assert(v);

TAJS_dumpValue('OK');
