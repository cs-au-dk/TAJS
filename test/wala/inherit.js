
function Polygon() {
  this.edges = 8;
  this.regular = false;
  this.shape = function shape() { return "rectangle"; };
  this.area = function area() { return -1; };
}

function objectMasquerading () {

  function Rectangle(top_len,side_len) {
    this.temp = Polygon;
    this.temp();
    this.temp = null;
    this.edges = 4;
    this.top = top_len;
    this.side = side_len;
    this.area = function area() { return this.top*this.side; };
  }

  return new Rectangle(3, 5);
}

function sharedClassObject() {

  function Rectangle(top_len, side_len) {
    this.edges = 4;
    this.top = top_len;
    this.side = side_len;
    this.area = function area() { return this.top*this.side; };
  }

  Rectangle.prototype = new Polygon();

  return new Rectangle(3, 7);
}

var rec1 = objectMasquerading();
assert(rec1.area() == 15);
assert(rec1.shape() == "rectangle");
dumpValue(rec1.area());
dumpValue(rec1.shape());
var rec2 = sharedClassObject();
assert(rec2.area() == 21);
assert(rec2.shape() == "rectangle");

