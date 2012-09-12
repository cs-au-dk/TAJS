function highest(basex){ 
  dumpValue(arguments);
  return arguments.sort(function(a,b){ // will fail, arguments is not an array!
    return a - b; 
  }).slice(0, basex); 
} 
assert(highest(1, 1, 2, 3).length == 1); 
assert(highest(3, 1, 2, 3, 4, 5)[2] == 3);
