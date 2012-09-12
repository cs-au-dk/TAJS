if (!Array.prototype.forEach) { 
  Array.prototype.forEach = function(fn){ 
    for ( var i = 0; i < this.length; i++ ) { 
      fn( this[i], i, this ); 
    } 
  }; 
} 
 
["a", "b", "c"].forEach(function(value, index, array){
  assert( value );
  dumpValue( value );
  dumpValue( index );
  dumpValue( array.length ); 
});
