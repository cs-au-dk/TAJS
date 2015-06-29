function bind(context, name){ 
  return function(){ 
    return context[name].apply(context, arguments); 
  }; 
} 
 
var Button = { 
  click: function(){ 
    this.clicked = true; 
  } 
}; 
 
var elem = document.createElement("li"); 
elem.innerHTML = "Click me!"; 
elem.onclick = bind(Button, "click"); 
document.getElementById("results").appendChild(elem); 
 
elem.onclick(); 
TAJS_assert( Button.clicked );
