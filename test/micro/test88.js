function Polygon() {
}

function Rectangle() {
    this.temp = Polygon;
    this.temp();
  }

new Polygon();
var rec1 = new Rectangle();

TAJS_dumpObject(rec1);
