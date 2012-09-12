// Array constructor test
var arr0 = new Array()
assert(arr0.length == 0)

var arr1 = new Array("test")
assert(arr1.length == 1)
assert(arr1[0] == "test");

if (Math.random())
	var t = 42;
else	
	var t = 24;	
var arr42 = new Array(t);
dumpObject(arr42);

var arr3 = new Array(0,1,2)
assert(arr3.length == 3)
assert(arr3[0] == 0)
assert(arr3[1] == 1)
assert(arr3[2] == 2)
