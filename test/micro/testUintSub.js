var num = Math.floor(Math.random());
var uint = Math.random() ? 1 : 2;
var constant = 1;

var uint_uint = uint - uint;

var uint_constant = uint - constant;
var constant_uint = constant - uint;

var uint_num = uint - num;
var num_uint = num - uint;

TAJS_dumpValue(uint_uint);

TAJS_dumpValue(uint_constant);
TAJS_dumpValue(constant_uint);

TAJS_dumpValue(uint_num);
TAJS_dumpValue(num_uint);