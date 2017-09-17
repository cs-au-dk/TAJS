function smallest(){ 
  return Math.min.apply( Math, arguments ); 
} 
function largest(){ 
  return Math.max.apply( Math, arguments ); 
} 
TAJS_assert(smallest(0, 1, 2, 3) == 0); 
TAJS_assert(largest(0, 1, 2, 3) == 3);
