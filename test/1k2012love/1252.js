// b = body, c = cvs, a = ctx
// Build steps: Run Closure Compiler advanced, remove newlines, remove last semicolon, wrap in IIFE, run First Crush

var w = this,
	r = "equestAnimationFrame",
	animFrame = w["r"+r] || w["webkitR"+r] || w["mozR"+r] || w["msR"+r] || w["oR"+r] || function (cb) { setTimeout(cb, 1000 / 60); };

var MATH = Math,
	PI = MATH.PI,
	FLOOR = MATH.floor,
	RAND = MATH.random;

var WIDTH = c.width = 960,
	HEIGHT = c.height = 540,
	hanabi = [],
	queue = 0;

function burst (x, y, size, speed, spread, count, delay) {
	var list = [],
		color = "#" + ["F20", "F0F", "F6B", "C18", "408", "800"][FLOOR(RAND() * 6)];
	
	for (; count--;) {
		var angle = PI * 2 * RAND(),
			velocity = speed * RAND();
		
		list.push({
			x: x, // x
			y: y, // y
			j: velocity * MATH.cos(angle), // vx
			k: velocity * MATH.sin(angle), // vy
			s: 0.97 + ((spread - 100) / 1000), // spread, 0-129
			r: size + FLOOR(RAND() * 6), // radius
			c: color // color
		});
	}
	
	setTimeout(function () {
		hanabi = hanabi.concat(list);
		
		queue--;
	}, delay * (1000 / 60));
}

function heart (x, y, s) {
	var s1 = .48 * s,
		s2 = .24 * s,
		s3 = .336 * s;
	
	a.beginPath();
	a.moveTo(x - s1, y);
	a.lineTo(x, y + s1);
	a.arc(x + s2, y - s2, s3, 0.25 * PI, 1.25 * PI, true);
	a.arc(x - s2, y - s2, s3, 1.75 * PI, 0.75 * PI, true);
	a.fill();
}

(function render () {
	if (!hanabi.length && !queue) {
		for (var count = queue = FLOOR(RAND() * 6); count--;) {
			burst(
				130 + FLOOR(RAND() * 701), // x
				130 + FLOOR(RAND() * 151), // y
				5 + FLOOR(RAND() * 16), // size
				4 + FLOOR(RAND() * 7), // speed
				70 + FLOOR(RAND() * 40), // spread
				50 + FLOOR(RAND() * 201), // count
				FLOOR(RAND() * 150) // delay
			);
		}
	}
	
	hanabi.forEach(function (h, i) {
		h.x += h.j; // x+vx
		h.y += h.k; // y+vy
		h.j *= h.s; // vx*spread
		h.k *= h.s; // vy*spread
		h.y += 1.1; // y+gravity
		
		a.fillStyle = !FLOOR(RAND() * 4) ? "rgba(256,256,256,.8)" : h.c;
		heart(h.x, h.y, h.r);
		
		if ((h.r *= h.s) < 0.1) {
			hanabi.splice(i, 1);
		}
	});
	
	a.fillStyle = "rgba(0,0,0,.3)";
	a.fillRect(0, 0, WIDTH, HEIGHT);
	
	animFrame(render);
})();

c.onclick = function (e) {
	queue++;
	
	burst(
		e.pageX - this.offsetLeft, // x
		e.pageY - this.offsetTop, // y
		5 + FLOOR(RAND() * 16), // size
		4 + FLOOR(RAND() * 7), // speed
		70 + FLOOR(RAND() * 40), // spread
		50 + FLOOR(RAND() * 201), // count
		0 // delay
	);
};

b.onkeypress = function () {
	[
		               [480,350],
		          [430,300],[530,300],
		     [380,250],          [580,250],
		[360,200],     [480,200],     [600,200],
		     [410,160],          [550,160]
	].forEach(function (xy) {
		queue++;
		
		burst(
			xy[0], // x
			xy[1], // y
			50, // size
			3, // speed
			50, // spread
			100, // count
			0 // delay
		);
	});
};