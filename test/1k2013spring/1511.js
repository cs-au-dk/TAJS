// Circles 1K
// by Evan Hahn (evanhahn.com)

// This code is licensed under the Unlicense. For more information, refer to
// <http://unlicense.org/>.

// Enjoy!

////////////////////////////////////////////////////////////////////////////////

// Globals are lowercase.
// Temporary variables are uppercase.

// Here's the list of globals:

// b = <body>
// c = <canvas>
// a = canvas context

// x, y = coordinates of circle
// r = radius of circle
// f = boolean, whether it's filled
// s = how often to draw a new circle

// d = function, draw the circle

// m = Math
// t = setTimeout

////////////////////////////////////////////////////////////////////////////////

// Initial drawing setup.
x = y = r = -1;
f = I = 0;  // I is used later
s = 50;

// Shorthands.
m = Math;
t = setTimeout;

// Style the <body>.
b.style.cssText = 'margin:0;background:#000;font-family:sans-serif;overflow:hidden';

// Make the canvas fill the window. Call it right now, but also bind it to the
// window's resize event. Also, focus the canvas.
(onresize = function() {
	w = c.width = innerWidth;
	h = c.height = innerHeight;
	c.focus();
})();

// Start drawing the circle.
(d = function() {

	// If we've started...
	// (r ^ -1) is equivalent to (r != -1)
	if (r ^ -1) {

		// Draw it!
		// We ALWAYS draw the stroke and only sometimes fill. This is just to save
		// bytes.
		with (a) {

			// What color? This is a shorthand for the following:
			// Math.floor(Math.random() * parseInt('FFFFFF', 16)).toString(16);
			fillStyle = strokeStyle = '#' + ((m.random() * (1 << 24))|0).toString(16);

			// Draw the arc.
			beginPath();
			arc(x, y, r, 0, 7);  // I use 7 because 7 > (2 * pi)
			stroke();
			if (f)
				fill();

		}

	}

	// Go again!
	t(d, s);

})();

// Move the figure.
// Make the radius's size proportional to how far away it was.
onmousemove = function(E) {
	X = E.clientX - x;
	Y = E.clientY - y;
	r = m.sqrt(X*X + Y*Y) * 2;
	x += X;
	y += Y;
};

// Toggle whether it's a solid dot.
onclick = function() {
	f = !f;
};

// Deal with keyboard stuff.
onkeyup = function(E) {

	// Spacebar clears everything.
	// We're also assigning the keycode to K here.
	if ((K = E.keyCode) == 32)
		a.clearRect(0, 0, w, h);

	// Change refresh speed with the arrow keys.
	if (K == 38)
		s -= 25;
	if (K == 40)
		s += 25;

	// s must be within [5, 150].
	s = m.min(m.max(s, 5), 150);

};

// Make start the notification and put it in the DOM.
(S = (L = document.createElement('div')).style).cssText = 'position:fixed;background:#fff;text-align:right';
S.top = S.right = S.padding = '20px';
L.innerHTML = '<b>circles!</b><br>mouse = draw<br>click = toggle solid<br>up = faster<br>down = slower<br>space = clear';
b.appendChild(L);

// Fade the start notification away.
t(function() {
	for (; I < 1E3; I += 10) {
		t(function(O) {
			S.opacity = O;
		}, I, 1 - (I / 1E3));
	}
	t(function() {
		S.display = 'none';
	}, 1E3);
}, 8E3);