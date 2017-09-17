function smallest(array){ 
  return Math.min.apply( Math, array ); 
} 
function largest(array){ 
  return Math.max.apply( Math, array ); 
} 
TAJS_assert(smallest([0, 1, 2, 3]) == 0); 
TAJS_assert(largest([0, 1, 2, 3]) == 3);
TAJS_dumpValue(smallest([0, 1, 2, 3]));
TAJS_dumpValue(largest([0, 1, 2, 3]));
