function multiMax(multi){ 
  // Make an array of all but the first argument 
  var allButFirst = Array().slice.call( arguments, 1 ); 

  // Find the largest number in that array of arguments 
  var largestAllButFirst = Math.max.apply( Math, allButFirst );

  // Return the multiplied result 
  return multi * largestAllButFirst;
} 
TAJS_assert( multiMax(3, 1, 2, 3) === 9);