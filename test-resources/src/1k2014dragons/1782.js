var M = Math,
	R = M.random,
	F = M.floor,
	C = M.cos,
	S = M.sin,
	P = M.PI,
	sX = 0,
	sY = 0,
	len = 30,
	r1 = F(50*R()),
	r2 = F(50*R()),
	ang1 = 2*P*R(),
	ang2 = 2*P*R(),
	rx1 = sX + len/2 + r1*C(ang1),
	ry1 = sY + r1*S(ang1),
	rx2 = sX + len/2 + r2*C(ang2),
	ry2 = sY + r2*S(ang2),
	w = c.width = c.height = M.max(window.innerWidth,window.innerHeight);

function drawBase(x0,y0,x1,y1,x2,y2,x3,y3) {
	c.moveTo(x0,y0);
	c.bezierCurveTo(x1,y1,x2,y2,x3,y3);
	c.save();
	c.translate(x3,y3);
	c.rotate(P);
	c.translate(-x3,-y3);
	c.bezierCurveTo(x2,y2,x1,y1,x0,y0);
	c.save();
	c.translate(x3,y3);
	c.rotate(P);
	c.translate(-x3,-y3);
}

function drawLines() {
	for(var i = 0; sX + 2*i*len < w; ++i) {
		for(var j = 0; sY + 2*j*len < w; ++j) {
			drawBase(sX + 2*i*len,sY + 2*j*len,rx1 + 2*i*len,ry1 + 2*j*len,rx2 + 2*i*len,ry2 + 2*j*len,sX + (2*i + 1)*len,sY + 2*j*len);
		}
	}
}

c.beginPath();

drawLines();
c.translate(w/2,w/2);
c.rotate(P/2);
c.translate(-w/2,-w/2);
drawLines();

c.lineWidth = 1;
c.stroke();