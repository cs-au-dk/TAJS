function addMethod(object, fn){ 
  var old = object.find; 
  object.find = function(){ 
    if (fn.length == arguments.length ) 
      return fn(); 
    else 
      return old.apply( this ); 
  }; 
} 
 
var ninjas = {}
addMethod(ninjas, function fff(x){  }); 
addMethod(ninjas, function ggg(y,z){  }); 
ninjas.find(42); 
