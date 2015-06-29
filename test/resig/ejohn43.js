function highest(basex){ 
  TAJS_dumpValue(arguments);
  return arguments.sort(function(a,b){ // will fail, arguments is not an array!
    return a - b; 
  }).slice(0, basex); 
} 
TAJS_assert(highest(1, 1, 2, 3).length == 1); 
TAJS_assert(highest(3, 1, 2, 3, 4, 5)[2] == 3);
