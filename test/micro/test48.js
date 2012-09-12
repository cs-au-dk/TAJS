function Polygon() {}

function objectMasquerading () {
  function Rectangle() {
    Polygon();
  }
  return new Rectangle();
}

var rec1 = objectMasquerading();
dumpObject(rec1);
