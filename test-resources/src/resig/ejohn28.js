function loop(array, fn){ 
  for ( var i = 0; i < array.length; i++ ) 
    fn.call( array, array[i], i ); 
} 
var num = 0; 
loop([0, 1, 2], function(value, i){
  TAJS_assert(value, 'isMaybeNumUInt||isMaybeSingleNum');
  TAJS_assert(num++, 'isMaybeNumUInt||isMaybeSingleNum');
  TAJS_assert(this instanceof Array); 
});
