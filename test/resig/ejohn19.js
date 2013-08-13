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
	 
dumpValue( getElements("pre") );
//dumpValue(getElements.cache.length);

dumpValue( getElements("pre") );
