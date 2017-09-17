function f(){
    try{
        try{
            for(x in {p: true}){
                return true;
            }
        }finally{
            
        }
    }finally{
        v = true;
    }
}
TAJS_assert(f());
TAJS_assert(v);
TAJS_assert(x === 'p');

TAJS_dumpValue('OK');
