// -nodejs -type-filtering test-resources/src/tsspecs/underscoretest/app.js

// var TAJS_dumpValue = console.log;
// var TAJS_dumpObject = console.log;

var u = require("underscore");
var a = {a: 1};
var b = {b: 2};
u.extend(a, b);
TAJS_dumpObject(a);
