// -nodejs -type-filtering test-resources/src/tsspecs/lodashtest/app.js

// var TAJS_dumpValue = console.log;
// var TAJS_dumpObject = console.log;

var isEqual = require('lodash.isequal'); // loads a one-function module
var obj1 = {username: 'peter'};
var obj2 = {username: 'peter'};
var obj3 = {username: 'gregory'};
TAJS_dumpValue(isEqual(obj1, obj2));
TAJS_dumpValue(isEqual(obj1, obj3));

var l = require("lodash");
TAJS_dumpValue(l.VERSION);
var vals = [-2, 0, 3, 7, -5, 1, 2];
TAJS_dumpValue(l.sum(vals));

var isEqualB = require('lodash/isequal'); // loads the entire module but uses only one function
TAJS_dumpValue(isEqualB(obj1, obj2));
TAJS_dumpValue(isEqualB(obj1, obj3));

