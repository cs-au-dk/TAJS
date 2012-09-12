var o = {x: 3}
if (Math.random() > 0.4)
	dumpValue(o.x)
o.pro = "seje"
if (Math.random() > 0.4)
	delete o.pro
dumpValue(o)
dumpValue(o.pro)
o.dead = "i am so dead"