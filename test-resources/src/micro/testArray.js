// Constructor tests
var arr = []
TAJS_dumpObject(arr)
var newArr0 = new Array()
TAJS_dumpObject(newArr0)
var newArr01 = [2]
TAJS_dumpObject(newArr01)
var newArr02 = ["a"]
TAJS_dumpObject(newArr02)
var newArr1 = new Array("test");
TAJS_dumpObject(newArr1)
var newArr2 = new Array(42, 45);
TAJS_dumpObject(newArr2)
var newArr42 = new Array(42)
TAJS_dumpObject(newArr42)
if (Math.random()) 
    var gt = {gt:23, bg:43}
else
    var gt = 42
TAJS_dumpValue(gt);

var newArrMNum = new Array(gt);
TAJS_dumpObject(newArrMNum);
if (Math.random()) 
    var yt = {gt:23, bg:43}
else
    if (Math.random()) var yt = 2;
    else var yt = 564;
TAJS_dumpValue(yt);

var newArrNNum = new Array(yt);
TAJS_dumpObject(newArrNNum);

// Length test
var arr = [1,2]
TAJS_dumpObject(arr);
arr[42] = "string"
TAJS_dumpObject(arr);

var arr = [1,2]
//arr[1] = 1000;
TAJS_dumpObject(arr);
arr["ni"] = 23;
TAJS_dumpObject(arr);
TAJS_dumpValue(arr.toString())

var arr = [];
TAJS_dumpValue(arr.length);
arr.length = 67;
TAJS_dumpValue(arr.length);
TAJS_assert(arr.length == 67);

var arr = [1,2,3,4,5,6,7,8,9,0]
arr.length = 3;
TAJS_dumpObject(arr);

if (Math.random())
    var maybeNum = 4;
else
    var maybeNum = 6;
TAJS_dumpValue(maybeNum);

var arr = [1,23,4]
arr[maybeNum] = 34;
TAJS_dumpObject(arr)

var arr = [1,2]
arr.length = maybeNum
TAJS_dumpObject(arr);
arr[7] = 32;
TAJS_dumpObject(arr);

// toString and join test
TAJS_dumpValue([].toString())
var arr = [1,2,3,undefined,4,5,6]
TAJS_dumpValue(arr.toString())
TAJS_dumpValue(arr.join("   "));
TAJS_dumpValue(arr.join(maybeNum))
var arr = [1,2,3,4,5,maybeNum,4]
TAJS_dumpValue(arr.toString())
if (Math.random())
    var lor = [9,2,8]
else
    var lor = [1,2]
TAJS_dumpValue(lor.toString())

//Concat
var arr1 = [1,3,4,5,"dfg"]
var arr2 = [1,43,"sdf",34]
TAJS_dumpObject(arr1.concat(arr2))
TAJS_dumpObject(arr1.concat({gt:6, bt:34}));
if (Math.random()) 
    var fr = {gt: 4}
else
    var fr = [1,2]
TAJS_dumpObject(arr1.concat(fr))

TAJS_dumpObject(arr1.concat("string"))

// Push and pop test
var arr = [1]
arr.push("string", 34);
TAJS_dumpObject(arr);
TAJS_dumpValue(arr.pop())
TAJS_dumpObject(arr);
arr.push(100);
arr.push(243);
TAJS_dumpObject(arr)
TAJS_dumpValue(arr.pop())
TAJS_dumpObject(arr);
TAJS_dumpValue([].pop())

// Reverse
TAJS_dumpObject([].reverse());
var arr = [0,1,2,3,4,5,6,7]
arr.reverse();
TAJS_dumpObject(arr);
arr.length = maybeNum;
arr.reverse()
TAJS_dumpObject(arr);

// Shift
arr = [];
TAJS_dumpValue(arr.shift());
TAJS_dumpObject(arr); 

var arr = [0,1,2,3,4,5,6,7,8,9]
TAJS_dumpValue(arr.shift());
TAJS_dumpObject(arr);

var arr = [0,1,2,3,4,5,6,7,8,9]
arr.length = 11;
TAJS_dumpValue(arr.shift());
TAJS_dumpObject(arr);

var arr = [0,1,2,3,4,5,6,7,8,9]
delete arr[2]
TAJS_dumpObject(arr)
TAJS_dumpValue(arr.shift());
TAJS_dumpObject(arr);

arr.length = maybeNum
TAJS_dumpValue(arr.shift());
TAJS_dumpObject(arr);
