// From TestV8. 
// 
// Once this test passes, uncomment the corresponding line in the in the substr
// test in TestV8.
//
// Currently gives the output:
//
// testsubstr1.js:1: [definite] Too few parameters to function String.prototype.substr
// testsubstr1.js:1: [info] Abstract value: Str

// Should not give any warnings and result in "asdf".
dumpValue("asdf".substr())
