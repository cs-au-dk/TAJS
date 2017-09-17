var dummyVar;
var v_no_prop = delete {}.p;
var v_prop = delete {p: 42}.p;
var v_var = delete dummyVar;

TAJS_dumpValue(v_no_prop);
TAJS_dumpValue(v_prop);
TAJS_dumpValue(v_var);
