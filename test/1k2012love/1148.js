c.width=g=window.innerWidth;
c.height=e=window.innerHeight;
with(b.style)marginLeft=marginTop=0,overflow="hidden";
f = 'px arial'
m = '#F00'
n = '#000'
h = '?'

k = function (l) {
	return l.toString().length == 1 ? "0" + l : l;
}

c.style.background=n;
z = function (x,y,t,o) { 							
	a.font = (g * 0.12)  + f;			
	a.fillStyle=a.strokeStyle= o ? '#FF6699' : m;				
	t ? a.fillText(h, x, y) : a.strokeText(h, x, y);
	a.font = (g * 0.012) + f;
	o ? a.fillText(k(j) + ":" + k(v) + ":" + k(s), x + (g * 0.013), y - (g * 0.043)) : a				
	o ? a.fillText("js1k", x + (g * 0.026), y - (g * 0.03)) : a
}

d = function(y, t) {
	for(w = 32, i = 0; w > 1, i < 6; w = w /2, i++) {
			z((c.width / 2) - (g / 3.6) + (i*(g / 10)), (c.height / 2) + (g / 6.9) - (y * (g / 9)), t >= w, (y == 2 && i == 0));
			t >= w ? t -= w : a						
	} 				 				
}

u = function() {
	a.clearRect(0, 0, g, e)
	p = new Date();
	s = p.getSeconds()
	v = p.getMinutes()
	j = p.getHours()
	d(0, s);
	d(1, v);
	d(2, j);
	setInterval("u()", 10);
}

u();