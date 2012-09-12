function katana(){ 
  this.isSharp = true; 
} 
katana(); 
assert( isSharp === true ); 
	 
var shuriken = { 
  toss: function(){ 
    this.isSharp = true; 
  } 
}; 
shuriken.toss(); 
assert( shuriken.isSharp === true );
