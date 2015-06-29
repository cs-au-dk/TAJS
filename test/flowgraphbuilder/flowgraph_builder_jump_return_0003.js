function f(){
    try{
        try{
            return;
        }finally{
            v = true;            
        }
    }finally{

    }
}
f();
TAJS_assert(v);

TAJS_dumpValue('OK');
