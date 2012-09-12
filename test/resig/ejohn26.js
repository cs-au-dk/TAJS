function add(a, b){ 
  return a + b; 
} 
assert( add.call(this, 1, 2) == 3 ); 
assert( add.apply(this, [1, 2]) == 3 );
