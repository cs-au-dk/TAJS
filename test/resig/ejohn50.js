var num = 10; 
 
function addNum(myNum){ 
  return num + myNum; 
} 
 
num = 15; 
 
TAJS_assert( addNum(5) == 20 ); // modified to pass
