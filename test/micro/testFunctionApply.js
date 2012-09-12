//// warm up
//function f(x, y) {
//	return x+y;
//}
//var a1 = f(17,4);
//dumpValue(a1);
//
//// invocation via apply without receiver
//function g(x, y) {
//	dumpValue(x);
//	dumpValue(y);
//	return x-y;
//}
var a2arg = new Array(2);
dumpObject(a2arg);
a2arg[0] = 101;
//a2arg[1] = 1;
//dumpObject(a2arg);
//var a2 = g.apply(null, a2arg);
//dumpValue(a2);
//
//// invocation with empty arguments
//function g0(x,y) {
//	return x-y;
//}
//var a3 = g0.apply(null, null);
//dumpValue(a3);
//
//// invocation without arguments
//var a4test = "banzai";
//function g1() {
//	return this.a4test;
//}
//var a4 = g1.apply();
//dumpValue(a4);
//
//// invocation with arguments object
//function g3(x, y) {
//	return x*y;
//};
//function g2() {
//	return g3.apply(null, arguments);
//}
//var a5 = g2(5, 4);
//dumpValue(a5);
//
//// invocation with unknown number of arguments
//function g4(x, y) {
//	return x / y;
//};
//function g5() {
//	return g4.apply(null, arguments);
//}
//var a6 = g5(0, 1);
//var a7 = g5(1, 2, 3);
//dumpValue (a6);
//dumpValue (a7);
