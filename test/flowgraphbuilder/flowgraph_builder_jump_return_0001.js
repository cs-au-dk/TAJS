function f(){
    try{
        try{
            return;
        }finally{
        
        }
    }finally{
        v = true;
    }
}
f();
TAJS_assert(v);

TAJS_dumpValue('OK');
