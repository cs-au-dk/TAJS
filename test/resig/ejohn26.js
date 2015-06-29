function add(a, b){ 
  return a + b; 
} 
TAJS_assert( add.call(this, 1, 2) == 3 ); 
TAJS_assert( add.apply(this, [1, 2]) == 3 );
