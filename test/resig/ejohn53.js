var resultsDiv = document.createElement('div');
resultsDiv.id = 'results';

var count = 1;
var elem = document.createElement("li");
elem.innerHTML = "Click me!"; 
elem.onclick = function(){ 
  TAJS_dumpValue( count++ );
};
document.getElementById("results").appendChild( elem );
TAJS_dumpValue( elem.parentNode );
