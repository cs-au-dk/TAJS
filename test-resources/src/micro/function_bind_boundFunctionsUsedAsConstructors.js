//Taken from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind
function Point(x, y) {
    this.x = x;
    this.y = y;
}

Point.prototype.toString = function() {
    return this.x + ',' + this.y;
};

var p = new Point(1, 2);
p.toString(); // '1,2'
TAJS_assert(p.toString() === '1,2');

// not supported in the polyfill below,

// works fine with native bind:

var YAxisPoint = Point.bind(null, 0/*x*/);


var emptyObj = {};
var YAxisPoint = Point.bind(emptyObj, 0/*x*/);

var axisPoint = new YAxisPoint(5);
axisPoint.toString(); // '0,5'
TAJS_assert(axisPoint.toString() === '0,5');

axisPoint instanceof Point; // true
TAJS_assert(axisPoint instanceof Point);

axisPoint instanceof YAxisPoint; // true
TAJS_assert(axisPoint instanceof YAxisPoint);

new Point(17, 42) instanceof YAxisPoint; // true
TAJS_dumpValue(new Point(17, 42) instanceof YAxisPoint);
TAJS_assert(new Point(17, 42) instanceof YAxisPoint);

