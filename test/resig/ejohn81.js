Object.prototype.keys = function(){ 
  var kk = []; 
  for ( var i in this ) 
    kk.push( i ); 
  return kk; 
}; 
 
var obj = { a: 1, b: 2, c: 3 }; 
 
assert( obj.keys().length == 3 ); 
dumpValue(obj.keys().length);

dumpValue(Object.prototype.keys); 
delete Object.prototype.keys;
dumpValue(Object.prototype.keys); 
