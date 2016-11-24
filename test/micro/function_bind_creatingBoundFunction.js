//Taken from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind
this.x = 9;
var module = {
    x: 81,
    getX: function() { return this.x; }
};

module.getX(); // 81
TAJS_assert(module.getX() === 81);

var retrieveX = module.getX;
retrieveX();
// returns 9. The function gets invoked at the global
// scope.
TAJS_assert(retrieveX() === 9);

// Create a new function with 'this' bound to module
// New programmers might confuse the
// global var x with module's property x
var boundGetX = retrieveX.bind(module);
boundGetX(); // 81
TAJS_assert(boundGetX() === 81);