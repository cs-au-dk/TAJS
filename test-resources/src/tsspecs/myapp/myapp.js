// -nodejs -type-filtering test-resources/src/tsspecs/myapp/myapp.js

// var TAJS_dumpValue = console.log;
// var TAJS_dumpObject = console.log;

var mylib = require('./mylib');
var res = mylib.myfun(Math.random() ? 'foo' : true, Math.random() ? 'bar' : {});
TAJS_dumpValue(res);
TAJS_dumpValue(mylib.mystr);
TAJS_dumpValue(mylib.myobj.myfield);
TAJS_dumpValue(mylib.myobj.myfield2);
TAJS_dumpValue(mylib.myobj.myfield3);
TAJS_dumpValue(mylib.myobj.myfield4);
var x = { myfield: Math.random() ? 117 : "dyt" };
mylib.myfun2(x);
TAJS_dumpValue(x.myfield);
TAJS_dumpValue(mylib.myfun3());
// TAJS_dumpValue(mylib.myarray1);
// TAJS_dumpValue(mylib.myarray2);
// TAJS_dumpValue(mylib.mytuple);
// TAJS_dumpObject(mylib.myarray1);
// TAJS_dumpObject(mylib.myarray2);
// TAJS_dumpObject(mylib.mytuple);

var now = require("date-now");
var n = now();
TAJS_dumpValue(n);

var base64 = require('./base-64');
var encodedData = base64.encode("hello");
var decodedData = base64.decode(encodedData);
TAJS_dumpValue(decodedData);
