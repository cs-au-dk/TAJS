c.width = w = window.innerWidth;
c.height = h = window.innerHeight;
b.style.margin = 0;
b.style.background="#000";
var angle = 0,
	offset = 0,
	flag = false,
	hue = 1;
function draw() {
	var i,
		size = 120,
		alpha = 0,
		s = 1,
		v = 1,
		c,
		h1,
		x,
		r1,
		g1,
		b1,
		m,
		red,
		blue,
		green;
	hue %= 360;
	h1 = hue / 60;
	if (hue < 0) {
	    hue += 360;
	}
	c = v * s;
	h1 = hue / 60;
	x = c * (1 - Math.abs(h1 % 2 - 1));
	m = v - c;
	switch (Math.floor(h1)) {
		case 0: r1 = c; g1 = x; b1 = 0; break;
		case 1: r1 = x; g1 = c; b1 = 0; break;
		case 2: r1 = 0; g1 = c; b1 = x; break;
		case 3: r1 = 0; g1 = x; b1 = c; break;
		case 4: r1 = x; g1 = 0; b1 = c; break;
		case 5: r1 = c; g1 = 0; b1 = x; break;
	}
	red = Math.floor((r1 + m) * 255);
	green = Math.floor((g1 + m) * 255);
	blue = Math.floor((b1 + m) * 255);
	a.clearRect(0, 0, w, h);
		for (i = 1; i < 27; i++) {
			a.font = size + 'px arial';
			a.save();
			a.translate(w / 2, h / 2);
			a.rotate(angle * i / Math.PI);
			a.lineWidth = 2;
			a.fillStyle = 'rgba(' + red + ',' + green + ',' + blue + ',' + alpha + ')';
			a.fillText('✿', i * offset, i * offset);
			a.strokeStyle = 'rgba(255,255,255,' + alpha + ')';
			a.strokeText('✿', i * offset, i * offset);
			a.restore();
			alpha += (1 / 26);
			size -= 4;
		}
	hue += 1;
	angle += 0.0288;
	if (offset > w / 26 * 0.1 || offset > h / 26 * 0.1) {
		flag = false;
	} else if (offset < 0) {
		flag = true;
	}
	if (flag) {
		offset += 0.0144;
	} else {
		offset -= 0.0144;
	}
}
window.onload = function () {
	setInterval(draw, 50);
};