var resultsDiv = document.createElement('div');
resultsDiv.id = 'results';

var Button = {
  click: function(){ 
    this.clicked = true; 
  } 
}; 
 
var elem = document.createElement("li"); 
elem.innerHTML = "Click me!"; 
elem.onclick = Button.click; 
document.getElementById("results").appendChild(elem);
TAJS_dumpValue(document.getElementById("results"))
 
elem.onclick(); 
TAJS_dumpValue( elem.clicked );
