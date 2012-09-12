function merge(root){ 
  for ( var i = 0; i < arguments.length; i++ ) 
    for ( var key in arguments[i] ) 
      root[key] = arguments[i][key]; 
  return root; 
} 
 
var merged = merge({name: "John"}, {city: "Boston"}); 
assert( merged.name == "John" ); 
assert( merged.city == "Boston" );
dumpValue(merged.name);
dumpValue(merged.city);
dumpObject(merged);
