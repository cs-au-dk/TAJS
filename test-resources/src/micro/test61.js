var obj = {}; 

TAJS_dumpValue(obj == false);
TAJS_dumpValue(obj == true);
TAJS_dumpValue(false == obj);
TAJS_dumpValue(true == obj);

TAJS_assert((obj == false) === false);
TAJS_assert((obj == true) === false);
TAJS_assert((false == obj) === false);
TAJS_assert((true == obj) === false);
