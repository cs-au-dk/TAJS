function f(v){return v.p;}
f({p: true})
if(f()){
    TAJS_dumpValue("true");
}else{
    TAJS_dumpValue("false");
}
