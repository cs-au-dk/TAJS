Function.prototype.bind = function(object){ 
  var fn = this; 
  return function(){ 
    return fn.apply(object, arguments); 
  }; 
}; 
 
var Button = { 
  click: function(){ 
    this.clicked = true; 
  } 
}; 
 
var elem = document.createElement("li"); 
elem.innerHTML = "Click me!"; 
elem.onclick = Button.click.bind(Button); 
document.getElementById("results").appendChild(elem); 
 
elem.onclick(); 
TAJS_assert( Button.clicked );
