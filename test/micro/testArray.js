// Constructor tests
var arr = []
dumpObject(arr)
var newArr0 = new Array()
dumpObject(newArr0)
var newArr01 = [2]
dumpObject(newArr01)
var newArr02 = ["a"]
dumpObject(newArr02)
var newArr1 = new Array("test");
dumpObject(newArr1)
var newArr2 = new Array(42, 45);
dumpObject(newArr2)
var newArr42 = new Array(42)
dumpObject(newArr42)
if (Math.random()) 
    var gt = {gt:23, bg:43}
else
    var gt = 42
dumpValue(gt);

var newArrMNum = new Array(gt);
dumpObject(newArrMNum);
if (Math.random()) 
    var yt = {gt:23, bg:43}
else
    if (Math.random()) var yt = 2;
    else var yt = 564;
dumpValue(yt);

var newArrNNum = new Array(yt);
dumpObject(newArrNNum);

// Length test
var arr = [1,2]
dumpObject(arr);
arr[42] = "string"
dumpObject(arr);

var arr = [1,2]
//arr[1] = 1000;
dumpObject(arr);
arr["ni"] = 23;
dumpObject(arr);
dumpValue(arr.toString())

var arr = [];
dumpValue(arr.length);
arr.length = 67;
dumpValue(arr.length);
assert(arr.length == 67);

var arr = [1,2,3,4,5,6,7,8,9,0]
arr.length = 3;
dumpObject(arr);

if (Math.random())
    var maybeNum = 4;
else
    var maybeNum = 6;
dumpValue(maybeNum);

var arr = [1,23,4]
arr[maybeNum] = 34;
dumpObject(arr)

var arr = [1,2]
arr.length = maybeNum
dumpObject(arr);
arr[7] = 32;
dumpObject(arr);

// toString and join test
dumpValue([].toString())
var arr = [1,2,3,undefined,4,5,6]
dumpValue(arr.toString())
dumpValue(arr.join("   "));
dumpValue(arr.join(maybeNum))
var arr = [1,2,3,4,5,maybeNum,4]
dumpValue(arr.toString())
if (Math.random())
    var lor = [9,2,8]
else
    var lor = [1,2]
dumpValue(lor.toString())

//Concat
var arr1 = [1,3,4,5,"dfg"]
var arr2 = [1,43,"sdf",34]
dumpObject(arr1.concat(arr2))
dumpObject(arr1.concat({gt:6, bt:34}));
if (Math.random()) 
    var fr = {gt: 4}
else
    var fr = [1,2]
dumpObject(arr1.concat(fr))

dumpObject(arr1.concat("string"))

// Push and pop test
var arr = [1]
arr.push("string", 34);
dumpObject(arr);
dumpValue(arr.pop())
dumpObject(arr);
arr.push(100);
arr.push(243);
dumpObject(arr)
dumpValue(arr.pop())
dumpObject(arr);
dumpValue([].pop())

// Reverse
dumpObject([].reverse());
var arr = [0,1,2,3,4,5,6,7]
arr.reverse();
dumpObject(arr);
arr.length = maybeNum;
arr.reverse()
dumpObject(arr);

// Shift
arr = [];
dumpValue(arr.shift());
dumpObject(arr); 

var arr = [0,1,2,3,4,5,6,7,8,9]
dumpValue(arr.shift());
dumpObject(arr);

var arr = [0,1,2,3,4,5,6,7,8,9]
arr.length = 11;
dumpValue(arr.shift());
dumpObject(arr);

var arr = [0,1,2,3,4,5,6,7,8,9]
delete arr[2]
dumpObject(arr)
dumpValue(arr.shift());
dumpObject(arr);

arr.length = maybeNum
dumpValue(arr.shift());
dumpObject(arr);
