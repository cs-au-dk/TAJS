/*
 *	DragonFlies by Anders Kjeldsen (And3k5)
 *	My very first project for js1k
 *	A 'particle' system where the particles is attracted to the mouse.
 *
 *	Internal notes:
 *		a:							the canvas
 *		b							document body
 *		c							context
 *		d:							remove child
 *		AudioContext:				audio context (also webkit)
 *		requestAnimationFrame:		requestAnimationFrame (also moz, webkit and ms)
 */

/*
 *	Lazy compression method
 */
with (Math) {
	/*
	 *	Shorten a few variables;
	 */
	var ra = random,
	ro = round,
	w = a.width,
	h = a.height,

	/*
	 *	random number w/ bitshift;
	 */
	rn = function () {
		return ro(ra() * 63)*4;
	},

	/*
	 *	Dragonfly class
	 */
	DF = function (x, y, dx, dy, f, lU) {
		var t = this;
		t.lstUpd = lU;
		t.c = [rn(),rn(),rn()];
		t.p = {
			x : x,
			y : y
		};
		t.di = {
			x : dx,
			y : dy
		};
		t.force = f;
		/*
		 *	Dragonfly update function
		 */
		t.u = function () {
			with (t) {
				// Timestamp for rendering speed
				var tm = Date.now(),

				// Angle direction to mouse position
				F = atan2(my - p.y, mx - p.x);

				// Attract to mouse position, force by distance
				di.x += cos(F) * (2.5 / dst(p.x, mx));
				di.y += sin(F) * (2.5 / dst(p.y, my));

				// Calculate force and check if dragonfly is near the boundaries
				var fd = force * ((tm - lstUpd) / 1000),
				dx = di.x * fd,
				dy = di.y * fd;
				if (p.x + dx < 0)
					di.x = abs(di.x);
				if (p.x + dx > w)
					di.x = -abs(di.x);
				if (p.y + dy < 0)
					di.y = abs(di.y);
				if (p.y + dy > h)
					di.y = -abs(di.y);
				dx = di.x * fd;
				dy = di.y * fd;
				p.x += dx;
				p.y += dy;

				// If dragonfly is still outside, regrenerate position
				if ((p.x < 0) || (p.x > w) || (p.y < 0) || (p.y > h)) {
					p.x = ro(ra() * w);
					p.y = ro(ra() * h);
					di.x = ra() * 2 - 1;
					di.y = ra() * 2 - 1;
				}

				// Apply timestamp to dragonfly
				lstUpd = tm;
			}
		};
	},

	/*
	 *	Distance between two points (on same x or y)
	 */
	dst = function (x1, x2) {
		return max(x1, x2) - min(x1, x2);
	},

	/*
	 *	Array of the dragonflies
	 */
	DFs = [],

	/*
	 *	Mouse position x and y
	 */
	l = function (o1,o2) {
		return o1==undefined?o2:o1;
	},
	mx = 0,
	my = 0;
	a.addEventListener("mousemove", function (e) {
		mx = l(e.offsetX,e.layerX);
		my = l(e.offsetY,e.layerY);
	});

	// Put dragonflies into the array
	for (var i = 0; i < 400; i++)
		DFs.push(new DF(ro(ra() * w), ro(ra() * h), ra() * 2 - 1, ra() * 2 - 1, 50, Date.now()));

	// Graphics
	var st0 = "rgba(0,0,0,";
	c.lineCap = "round";
	c.lineWidth = 2;
	c.fillStyle = st0 + (1 + ")");
	c.fillRect(0, 0, w, h);
	(function gr() {
		c.fillStyle = st0 + (1 / 4 + ")");
		c.fillRect(0, 0, w, h);
		DFs.forEach(function (e) {
			/*c.fillStyle=*/
			c.strokeStyle = "rgb(" + e.c.join() + ")";
			c.beginPath();
			c.moveTo(e.p.x, e.p.y);
			e.u();
			c.lineTo(e.p.x, e.p.y);
			c.stroke();
		});
		requestAnimationFrame(gr);
	})();
}