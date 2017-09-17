// Array constructor test
var arr0 = new Array()
TAJS_assert(arr0.length == 0)

var arr1 = new Array("test")
TAJS_assert(arr1.length == 1)
TAJS_assert(arr1[0] == "test");

if (Math.random())
	var t = 42;
else	
	var t = 24;	
var arr42 = new Array(t);
TAJS_dumpObject(arr42);

var arr3 = new Array(0,1,2)
TAJS_assert(arr3.length == 3)
TAJS_assert(arr3[0] == 0)
TAJS_assert(arr3[1] == 1)
TAJS_assert(arr3[2] == 2)
