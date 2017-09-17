var o = {x: 3}
if (Math.random() > 0.4)
	TAJS_dumpValue(o.x)
o.pro = "seje"
if (Math.random() > 0.4)
	delete o.pro
TAJS_dumpValue(o)
TAJS_dumpValue(o.pro)
o.dead = "i am so dead"