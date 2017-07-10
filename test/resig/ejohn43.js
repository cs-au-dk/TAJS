function highest(basex){ 
  TAJS_dumpValue(arguments);
  return arguments.sort(function(a,b){ // will fail, arguments is not an array!
    return a - b; 
  }).slice(0, basex); 
}
highest(1, 1, 2, 3)
TAJS_assert(false);