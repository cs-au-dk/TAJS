function smallest(){ 
  return Math.min.apply( Math, arguments ); 
} 
function largest(){ 
  return Math.max.apply( Math, arguments ); 
} 
assert(smallest(0, 1, 2, 3) == 0); 
assert(largest(0, 1, 2, 3) == 3);
