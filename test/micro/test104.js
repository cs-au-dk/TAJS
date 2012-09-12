//function f(x) {
//	return x+1;
//}
//
//var a = 42;
//var b = f(a);
//var c = this.f(a);

var x={
 	a:function() {return 42}, 
 	b:function() {return this.a()},
 	c:function() {return a()}
}

var ta = x.a();
var tb = x.b();
var tc = x.c(); // SHOULD FAIL
