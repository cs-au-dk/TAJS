var a = "string"
var b = 1000
var ab = a+b 
TAJS_dumpValue(ab);


function X() {
    this.Y = 0;
}
var object = new X()
var s = object.Y + 1;
TAJS_dumpValue(s) 
