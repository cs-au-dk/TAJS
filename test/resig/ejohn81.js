Object.prototype.keys = function(){ 
  var kk = []; 
  for ( var i in this ) 
    kk.push( i ); 
  return kk; 
}; 
 
var obj = { a: 1, b: 2, c: 3 }; 
 
//TAJS_assert( obj.keys().length == 3 );
TAJS_dumpValue(obj.keys().length);

TAJS_dumpValue(Object.prototype.keys); 
delete Object.prototype.keys;
TAJS_dumpValue(Object.prototype.keys); 
