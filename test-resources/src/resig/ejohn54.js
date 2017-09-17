function Ninja(){ 
  var slices = 0; 
   
  this.getSlices = function(){ 
    return slices; 
  }; 
  this.slice = function(){ 
    slices++; 
  }; 
} 
 
var ninja = new Ninja(); 
ninja.slice(); 
TAJS_assert( ninja.getSlices() == 1 ); 
TAJS_assert( ninja.slices === undefined );
