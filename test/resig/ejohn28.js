function loop(array, fn){ 
  for ( var i = 0; i < array.length; i++ ) 
    fn.call( array, array[i], i ); 
} 
var num = 0; 
loop([0, 1, 2], function(value, i){ 
  assert(value == num++); 
  assert(this instanceof Array); 
});
