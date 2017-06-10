TAJS_makeContextSensitive(f_c, 0);

function f_c(a){
    return a;
}

f_c(Math.random());

var v1_c = f_c(1);

f_c(Math.random());

var v2_c= f_c(2);

f_c(Math.random());

var v3_c = v1_c + v2_c;

f_c(Math.random());

TAJS_assert(v3_c, 'isMaybeSingleNum');
