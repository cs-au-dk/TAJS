b.style.margin = 0;
b.style.overflow = "hidden";
M = Math;

c.width = innerWidth;
c.height = innerHeight;

CX = c.width / 2 | 0;
CY = c.height / 2 | 0;

l = [];
for (i = 10; i--;) {  l.push([CX, CY, M.random() * M.PI * 2]); }

a.fillStyle = "#000";
a.strokeStyle = "#9ff";
a.lineWidth = 0.01;
a.globalCompositeOperation = "lighter";
a.fillRect(0, 0, c.width, c.height);

f = 0;
s = 5;

setInterval(function() {
	if (!f && l.length <= 500) {
		l.push([CX, CY, M.random() * M.PI * 2]);
	} else if (!f) {
		a.fillStyle = "rgba(255,255,255,0.01)";
		a.strokeStyle = "#000";
		a.lineWidth = 0.5;
		a.globalCompositeOperation = "source-over";

		l = [];
		for (i = 1000; i--;) { l.push([CX, CY, M.random() * M.PI * 2]); }

		f = 1;
		s = 10;

	} else { a.fillRect(0, 0, c.width, c.height); }

	a.beginPath();

	for (i = l.length; i--;) {
		z = l[i][2];
		a.lineTo(l[i][0], l[i][1], 1, 1);
		l[i][0] += s * M.cos(z) | 0;
		l[i][1] += s * M.sin(z) | 0;
		l[i][2] += 0.1 * M.sqrt(z / (z * 180 / M.PI));
	}

	a.stroke();
}, 1000 / 60);