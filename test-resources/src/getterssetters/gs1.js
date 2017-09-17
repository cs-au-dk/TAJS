var rectangle = {
  height: 20,
  width: 10,
  get area() {
    TAJS_dumpValue(this);
    return this.height * this.width;
  },
  set area(val) {
    TAJS_dumpValue(val);
    throw "I’m sorry Dave, I’m afraid I can’t do that";
  }
}
TAJS_dumpObject(rectangle);
//TAJS_assert(typeof rectangle.area === 'number');
TAJS_dumpValue(rectangle.area); // calls the getter
try {
  rectangle.area = 42; // calls the setter
  TAJS_dumpValue("shouldn't be here");
  TAJS_assert(false);
} catch (e) {
  TAJS_dumpValue(e);
}
TAJS_dumpValue(rectangle.area);
TAJS_assertEquals(200, rectangle.area);
