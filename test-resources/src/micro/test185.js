// From TestV8. 
// 
// Once this test passes, uncomment the corresponding line in the to_precision 
// test in TestV8.

var rounded = (-1.2345e+27).toPrecision(4) + '';
// TAJS_dumpValue(rounded);
TAJS_assert(rounded === '-1.234e+27');
