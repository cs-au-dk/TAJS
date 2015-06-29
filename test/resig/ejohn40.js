function merge(root){ 
  for ( var i = 0; i < arguments.length; i++ ) 
    for ( var key in arguments[i] ) 
      root[key] = arguments[i][key]; 
  return root; 
} 
 
var merged = merge({name: "John"}, {city: "Boston"}); 
//TAJS_assert( merged.name == "John" );
//TAJS_assert( merged.city == "Boston" );
TAJS_dumpValue(merged.name);
TAJS_dumpValue(merged.city);
TAJS_dumpObject(merged);
