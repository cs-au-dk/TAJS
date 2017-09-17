var b = Math.random() == 0.4
TAJS_dumpValue(b)
var y = 0;
var str = "if (b) y=3; else y=true";
eval(str);
TAJS_dumpValue(y)