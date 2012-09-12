function highest(){ 
  return makeArray(arguments)
} 
 
function makeArray(array){ 
  dumpValue(array)
  return Array().slice.call( array ); 
} 
 
dumpObject(highest(1, 1, 2, 3)); 

