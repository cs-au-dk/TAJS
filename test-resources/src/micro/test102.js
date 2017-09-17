var i = 0;
var j = 799;
TAJS_dumpValue(i);
TAJS_dumpValue(j);
while (i <= j) {
	TAJS_dumpValue("foo");
	i++;
} 
TAJS_dumpValue(i);
TAJS_dumpValue("bar");
