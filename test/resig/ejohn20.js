function isPrime( num ) { 
  var prime = num != 1; // Everything but 1 can be prime 
  for ( var i = 2; i < num; i++ ) { 
    if ( num % i == 0 ) { 
      prime = false; 
      break; 
    } 
  } 
  return prime; 
} 
	 
TAJS_assert( isPrime(5), 'isMaybeAnyBool' );
