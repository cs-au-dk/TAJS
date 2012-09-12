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
	 
dumpObject( getElements("pre") );
dumpValue(getElements.cache.length)