var katana = { 
  isSharp: true, 
  use: function(){ 
    this.isSharp = !this.isSharp; 
  } 
}; 
katana.use() 
TAJS_assert( !katana.isSharp );
