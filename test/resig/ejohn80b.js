if (!Array.prototype.forEach2) { 
  Array.prototype.forEach2 = function(fn){ 
    for ( var i = 0; i < this.length; i++ ) { 
      fn( this[i], i, this ); 
    } 
  }; 
} 
 
["a", "b", "c"].forEach2(function(value, index, array){
  TAJS_assert( value, 'isMaybeSingleStr||isMaybeStrIdentifierParts||isMaybeUndef' );
  TAJS_dumpValue( value );
  TAJS_dumpValue( index );
  TAJS_dumpValue( array.length ); 
});
