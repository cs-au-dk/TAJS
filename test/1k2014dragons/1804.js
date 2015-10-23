u = t = 1;
e = function(A, B, C, D, E, F, G, H) {
	H = F < E;
	c.beginPath();
	c.fillStyle = A || "#000";
	for (c.arc(B, H * D * 2 + C, D, E, F, H); H < G * 4; H += 2)
		c.lineTo(H > 1 ? 500 : 400, 220 - Math.sin(t + 1) * 25);
	c.fill();
	A < "#9" && c.stroke()
};

w = function(A, B, C, D, E, F, G, H) {
	x = 520 + B * 5, y = 250;
	for (i = B; i++ < 40;)
		p = i > 35,
		y += 5 / (i < 7 ? 7 - i : i - 5),
		x += i / 7 - p, r = p ? 40 : 15 - i / 5,
		e(0, x - p * 30 + 25, y + Math.sin(t + x / 130 - p / 5 + .3) * 25 + p * 30, r, p ? 5 : 2, 6),
		e(0, 999 - x * 1.3 - p * 10, y + Math.sin(t + x / 130 - .3) * 25 + p * 35, r + 5, p ? 5.3 : 2, 6.3);
	for (i = 0; i++ < 5;)
		e(0, 150 + A + i * 60 + Math.sin(t + 1.3) * 30, 160 - i * 10 + Math.sin(t + 1.3) * (160 - A), 70, Math.sin(t + 1.3) > .3 ? 5 : 7, Math.sin(t + 1.3) > .3 ? 4 : 8, 1)
};

q = function(A, B, C, D, E, F, G, H) {
	;
	for (i = F; G < 0 ? i > 300 : i < 1050; i += G + i / B)
		p = i > 950 && i < 1040 && G < 1,
		x = i < 575 ? i : i * .4 + 345,
		r = i < 210 ? i / 23 : (i > 550 ? 25 - i / 105 : 19) - Math.sin(i / 62 + 4.3) * 10,
		e(p / G ? "#999" : H, x - D * 3 + (10 - u) * (A > 5), 300 - C * r - Math.sin(i / 99) * (20 + u * (A > 5) * 2) - Math.sin(i / 500 + t) * 25 + p * r + A - x / 8, r, D, E)
};

setInterval(i = function(A, B, C, D, E, F, G, H) {
	e("rgba(60,30,95,.5)", 999, 999, 9999, 0, 7);
	u *= .9;
	for (i = 5; i < 15; i += .5)
		e("rgba(0,0,0,.05)", 1500 - i * t % 250 * i + Math.sin(i) * 250, i * 15, i * 40, 0, 7);
	for (i = 50; i++ < 250;)
		Math.sin(i / 300 - t / 4) > .9 && [
			u += (u < 5) / 5,
			e("rgba(250," + (250 - i) + ",0," + (Math.sin(i + t * 2) / 5) + ")", 600 + i * 3, 190 - Math.sin(i / 60 - t) * 25 + i / 2, i / 2, Math.sin(i + t * 2) - 1, Math.sin(i + t * 2) + 2)
		];
	A = Math.sin(t += .2) / 9;
	w(60, 0);
	q(-10, 999, 0, 0, 5, 600, -5, "#102");
	q(5, 40, 2, 5, 6.5, 32, 0, "#102");
	q(u + 10, 40, 2, 6, 7, 980, 0);
	q(u + 20, 999, 1, 3, 5, x, 2);
	q(0, 999, 1, 0, 7, 20, 2);
	q(0, 0, 1.2 + A, 4.2 + A, 6.2 + A, 900, 2, "#3c0");
	q(0, 0, 1.2 + A, .5 + 4.2 + A, 6.2 + A, 905, 2);
	w(0, 2)
}, 50)