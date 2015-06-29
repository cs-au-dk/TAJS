function katana(){ 
  this.isSharp = true; 
} 
katana(); 
TAJS_assert( isSharp === true ); 
	 
var shuriken = { 
  toss: function(){ 
    this.isSharp = true; 
  } 
}; 
shuriken.toss(); 
TAJS_assert( shuriken.isSharp === true );
