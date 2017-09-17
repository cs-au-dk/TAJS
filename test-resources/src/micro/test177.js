function highest(){ 
  return makeArray(arguments)
} 
 
function makeArray(array){ 
  TAJS_dumpValue(array)
  return Array().slice.call( array ); 
} 
 
TAJS_dumpObject(highest(1, 1, 2, 3)); 

