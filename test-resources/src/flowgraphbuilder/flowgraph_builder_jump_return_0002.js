function f(){
    try{
        try{
            return;
        }finally{
            v = false;
        }
    }finally{
        v = v === false;
    }
}
f();
TAJS_assert(v);

TAJS_dumpValue('OK');
