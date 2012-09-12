var katana = { 
  isSharp: true, 
  use: function(){ 
    this.isSharp = !this.isSharp; 
  } 
}; 
katana.use() 
assert( !katana.isSharp );
