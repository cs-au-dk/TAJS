var x = [5,3,8];
x.sort();
TAJS_dumpObject(x);

var y = [5,3,8];
try {
  y.sort(42);
  TAJS_dumpObject(y);
} catch (e) {
  TAJS_dumpObject(y);
}
TAJS_dumpObject(y);

var z = [5,3,8];
try {
  z.sort(Math.random()>0.5 ? 42 : undefined);
  TAJS_dumpObject(z);
} catch (e) {
  TAJS_dumpObject(z);
}
TAJS_dumpObject(z);

//var q = [5,3,8];
//try {
//  q.sort(Math.random); // weird but possible (implementation-defined behavior because not consistent comparison function)
//  q.sort(eval); // weird but possible (implementation-defined behavior because not consistent comparison function)
//  TAJS_dumpObject(q);
//} catch (e) {
//  TAJS_dumpObject(q);
//}
//TAJS_dumpObject(q);



