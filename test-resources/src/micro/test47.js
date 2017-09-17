
function Polygon() {
  this.edges = 8;
}

function objectMasquerading () {

  function Rectangle(top_len,side_len) {
    this.temp = Polygon;
    this.temp();
    this.temp = null;
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

var rec2 = sharedClassObject();

TAJS_dumpObject(rec1);
TAJS_dumpObject(rec2);
