// From TestV8. 
// 
// Once this test passes, uncomment the corresponding line in the array_length 
// tests in TestV8.
//
// Currently gives the output:
//
// testarray1.js:6: [definite] RangeError, assigning invalid value to array 'length' property
// testarray1.js:6: [definite] Dead assignment, property valueOf is never read
// testarray1.js:6: [definite] Unreachable function
//
// 

a = []
a.length = { valueOf : function () { return 42; }}

TAJS_assert(a.length === 42);
