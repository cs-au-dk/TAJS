//Setting length with a field ref.
var gt = new Array(1,2,3,4,5,6,7,8,9)
var a ="gth";
gt["len" + a] = 3;
assert(gt.length == 3)
dumpObject(gt)

//Change length by simple assign
var arr = new Array(0,1,2,3,4,5,6,7,8,9)
assert(arr.length == 10);
arr[3] = 1000;
assert(arr.length == 10)
arr[42] = 1000;
assert(arr.length == 43)
dumpObject(arr)
