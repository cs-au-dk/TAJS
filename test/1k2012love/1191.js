w = c.width = 200;
h = c.height = 200;
m = Math;
d = m.abs;
r = m.random;
l = 4;
c.setAttribute("style","position:absolute;top:0;left:0;width:100%;height:100%;");
q = window;
e = [];

function sqDist(x1, y1, x2, y2) {
	var x = x1 - x2, y = y1 - y2;
	return x*x + y*y;
}

function getValue(hx, hy, x, y) {
	return m.min(2, 10 / (d(hx - x)+d(hy - y)) + m.max(100/sqDist(x,y,hx-13,hy-10), 100/sqDist(x,y,hx+13,hy-10)));
}

function draw() {
	var x,y,value,g,v,closest,max;
	a.fillStyle = "#"+((16+120*(m.sin((new Date().getTime())/900)+1))<<16).toString(16);
	a.fillRect(0,0,w,h);
	for(x=0;x<w;x++) {
		for(y=0;y<h;y++) {
			value = max = 0;
			for(i=0;i<l;i++) {
				v = getValue(e[i][0],e[i][1],x,y);
				if(v > max) {
					closest = i;
					max = v;
				}
				value += v;
			}
			a.fillStyle = "#"+e[closest][4].toString(16);
			if(value > 0.9) a.fillRect(x,y,1,1);
		}
	}
	for(i=1;i<l;i++) {
		g = e[i];
		g[0] += g[2];
		g[1] += g[3];
		if(g[0] < 0 || g[0] > w) g[2] *= -1;
		if(g[1] < 0 || g[1] > h) g[3] *= -1;
	}
}
c.onmousemove = function(v){
	e[0][0] = v.pageX / q.innerWidth * w;
	e[0][1] = v.pageY / q.innerHeight * h;
};
q.setInterval(draw, 40);
q.onmousedown = function(v) {
	v.stopPropagation();
	var b = v.which?v.which:v.button,i=0;
	if(b > 1) {
		for(i=0;i<l;i++) { 
			e[i][2] = r()*10; e[i][3] = r()*10; 
		}		
	} else {
		e = []; 
		for(i=0;i<l;i++) 
			e[i] = [r()*w,r()*h,r()*10,r()*10,(16+240*r())<<16];
	}
	return false;
}
q.onload = function(v) {q.onmousedown(v); c.onmousemove(v);}