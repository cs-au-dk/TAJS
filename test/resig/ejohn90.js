function addMethod(object, name, fn){ 
	  TAJS_dumpValue(name);
	  TAJS_dumpValue(object);
	  TAJS_dumpValue(fn);
  // Save a reference to the old method 
  var old = object[ name ]; 
 
  // Overwrite the method with our new one 
  object[ name ] = function(){ 
    // Check the number of incoming arguments, 
    // compared to our overloaded function 
    if ( fn.length == arguments.length ) 
      // If there was a match, run the function 
      return fn.apply( this, arguments ); 
 
    // Otherwise, fallback to the old method 
    else if ( typeof old === "function" ) 
      return old.apply( this, arguments ); 
  }; 
} 
 
function Ninjas(){ 
  var ninjas = [ "Dean Edwards", "Sam Stephenson", "Alex Russell" ]; 
  addMethod(this, "find", function(){ 
    return ninjas; 
  }); 
  addMethod(this, "find", function(name){ 
    var ret = []; 
    for ( var i = 0; i < ninjas.length; i++ ) 
      if ( ninjas[i].indexOf(name) == 0 ) 
        ret.push( ninjas[i] ); 
    return ret; 
  }); 
  addMethod(this, "find", function(first, last){ 
    TAJS_dumpValue(this);
	TAJS_dumpValue(first);
	TAJS_dumpValue(last);
    var ret = []; 
    for ( var i = 0; i < ninjas.length; i++ ) 
      if ( ninjas[i] == (first + " " + last) ) 
        ret.push( ninjas[i] ); 
    return ret; 
  }); 
} 
 
var ninjas = new Ninjas(); 
TAJS_assert( ninjas.find().length, 'isMaybeNumUInt');
TAJS_assert( ninjas.find("Sam").length, 'isMaybeNumUInt');
TAJS_assert( ninjas.find("Dean", "Edwards").length, 'isMaybeNumUInt');
TAJS_assert( ninjas.find("Alex", "X", "Russell"), 'isMaybeUndef||isMaybeObject');
