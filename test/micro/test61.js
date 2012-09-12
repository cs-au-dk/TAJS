var obj = {}; 

dumpValue(obj == false);
dumpValue(obj == true);
dumpValue(false == obj);
dumpValue(true == obj);

assert((obj == false) === false);
assert((obj == true) === false);
assert((false == obj) === false);
assert((true == obj) === false);
