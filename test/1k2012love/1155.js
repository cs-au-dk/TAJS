(function () { // stack refers to registered patterns
  var DEBUG = typeof process !== 'undefined' && process.env.DEBUG;
  function log() { if(DEBUG) console.log.apply(this,arguments); }
  function match(pattern, value) {
    if(pattern === Error) { 
      return value && value.name.indexOf('Error') !== -1;}
    if(pattern === undefined) return true;
    if(typeof pattern === 'object')
      return JSON.stringify(pattern) === JSON.stringify(value);
    return pattern.toString() === value.toString(); }
  function p() { var stack = [], arity;
    return function () {
    if(!arity) { arity = arguments.length-1; } // set arity in first invok.
    if(arity===arguments.length) { // # arguments match arity, execute
      var j = 0, i=0; // we need explicit control over vars
      ol: for(; i<arguments.length; i++) { // for each argument
        il: for(; j<stack.length; j++) { // for pattern in the stack
          var s = stack[j]; // get the current pattern
          log('α ', i, j);
          log('  ░ ', s, s[i]);
          log('  σ ', s.length);
          log('  • ', [].slice.call(arguments,0));
          if(s.length > i) { // if there's something in this pos for pattern
            if(match(s[i], arguments[i])) { // if we have a match
              log('  ✔ ', s[i], '===', arguments[i]);
              if(arguments.length !== i+1) { log('  ⥁'); continue ol; } }
            else {  // if it doesnt match try next pattern in stack
              log('  ✗ ', s[i], '===', arguments[i]);
              i=0;
              // dont break and set ok to false if this is the last element
              if(stack.length!==j+1) { log('  ⥁'); continue; } } }
         var f = s[s.length-1];
         log('  ' + (typeof f === 'function' ? 'ƒ' : 'λ'), f.name || f);
         // execute whatever is the last argument on last pattern of stack
         return (typeof f === 'function') ? // is there a callback?
           f.apply(this, [].slice.call(arguments,0)) : null; } }
    } else {
      stack.push([].slice.call(arguments,0)); // initializing add pattern
      log('‣ ', [].slice.call(arguments,0));
    } }; } // dont cache, each require is a new inst.
  typeof exports === 'undefined' ? (window.pattern=p) : (module.exports = p);
})();