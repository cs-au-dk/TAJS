function smallest(array){ 
  return Math.min.apply( Math, array ); 
} 
function largest(array){ 
  return Math.max.apply( Math, array ); 
} 
assert(smallest([0, 1, 2, 3]) == 0); 
assert(largest([0, 1, 2, 3]) == 3);
dumpValue(smallest([0, 1, 2, 3]));
dumpValue(largest([0, 1, 2, 3]));
