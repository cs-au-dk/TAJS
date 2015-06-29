function highest(basex){ 
  return makeArray(arguments).sort(function(a,b){ 
    return a - b; 
  }).slice(0, basex); 
} 
 
function makeArray(array){ 
  return Array().slice.call( array ); 
} 
 
TAJS_assert(highest(1, 1, 2, 3).length == 1); 
TAJS_assert(highest(3, 1, 2, 3, 4, 5)[2] == 3);

TAJS_dumpValue(highest(1, 1, 2, 3).length); 
TAJS_dumpValue(highest(3, 1, 2, 3, 4, 5)[2]);

