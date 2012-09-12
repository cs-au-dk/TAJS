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
assert( ninja.getSlices() == 1 ); 
assert( ninja.slices === undefined );
