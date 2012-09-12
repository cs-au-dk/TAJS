if (!Array.prototype.forEach2) { 
  Array.prototype.forEach2 = function(fn){ 
    for ( var i = 0; i < this.length; i++ ) { 
      fn( this[i], i, this ); 
    } 
  }; 
} 
 
["a", "b", "c"].forEach2(function(value, index, array){
  assert( value );
  dumpValue( value );
  dumpValue( index );
  dumpValue( array.length ); 
});
