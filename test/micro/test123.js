y = {f: 0, o: 1, t:2}
yy = {g: 0, e: 1, d: 2}
r =  ""
for (var x in y) {
	for (var xx in yy) {
		r = r + x +xx
	} 
}		

dumpValue(r)
