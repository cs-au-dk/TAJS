function getElements( name ) { 
  var results; 
	 
  if ( getElements.cache[name] ) { 
    results = getElements.cache[name]; 
  } else { 
    results = document.getElementsByTagName(name); 
    getElements.cache[name] = results; 
  } 
	 
  return results; 
} 
getElements.cache = {}; 
	 
TAJS_dumpValue( getElements("pre") );
//TAJS_dumpValue(getElements.cache.length);

TAJS_dumpValue( getElements("pre") );
