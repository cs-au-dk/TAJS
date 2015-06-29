var count = 1; 
var elem = document.createElement("li"); 
elem.innerHTML = "Click me!"; 
elem.onclick = function(){ 
  log( "Click #", count++ ); 
}; 
document.getElementById("results").appendChild( elem ); 
TAJS_assert( elem.parentNode );
