// Initialize some variables and make a hue table to speed up rendering.
for (b = [e = [g = [d = h = j = N = 5]]], i = 360; i--; g[i] = c.fillStyle) c.fillStyle = "hsl(" + i + ",100%,50%)";

// This function was discovered after countless hours of playing around in processing with rendering various types of trees.
f = function (a, d, h, j, v) {
	var w = j - d,
		x = v - h,
		y = Math.sqrt(w * w + x * x),
		z = d + w * k,
		A = h + x * k,
		B = d + .5 * w,
		C = h + .5 * x,
		D = d + w * l,
		E = h + x * l,
		G = x >>= 2; // The shift here and the one below is what makes the partices squiggly. You can replace it with /= 4 for smooth partices.
	N > a && ( // Only recurse if the recursion depth counter (a) is less than the max (N)
		// This bit uses some trig identities to avoid actually doing any trig at all. In the end it results in much higher speeds and unreadable code.
		w >>= 2, 
		x = m * w + q * x,
		w = q * w - m * G,
		f(a + 1, z, A, B += w, C += x),
		G = x,
		x = n * w + r * x,
		w = r * w - n * G,
		f(a + 1, D, E, B += w, C += x),
		G = x,
		x = o * w + s * x,
		w = s * w - o * G,
		f(a + 1, z, A, B += w, C += x),
		G = x,
		x = p * w + u * x,
		w = u * w - p * G,
		f(a + 1, D, E, B += w, C += x));
	a > 1 && ( // Don't render the first iteration, it's lame. Just two partices moving diagonally back and forth.
		i % 9 || c.beginPath(c.stroke(c.strokeStyle = g[~~ (400 * t + y) % 360])), // Only change the colour and stroke every 9 particles for a speed up.
		c.moveTo(F?j:b[i], F?v:e[i]), c.lineTo(b[i] = ~~j, e[i++] = ~~v), // Draw particles. My quantizing to integer coords with ~~, we avoid a nasty
		c.moveTo(F?d:b[i], F?h:e[i]), c.lineTo(b[i] = ~~d, e[i++] = ~~h)) // antialiasing bug in chrome that causes them to leave permanent trails.
},

/*
Chrome only
onmousemove = function (a) {
    a.which ? j += L - a.clientX : 0, // If the mouse button is pressed, add the movement delta to the time offset
	L = a.clientX // Set last mouse coord
},*/

/*
Firefox only
onmousemove = function (a) {
    a.buttons ? j += L - a.clientX : 0, // If the mouse button is pressed, add the movement delta to the time offset
	L = a.clientX // Set last mouse coord
},*/

/*
Chrome/Firefox only
onmousemove = function (a) {
    (a.buttons + 1 ? a.buttons : a.which) ? j += L - a.clientX : 0, // If the mouse button is pressed, add the movement delta to the time offset
	L = a.clientX // Set last mouse coord
},
*/

// Should work everywhere
onmousedown = onmouseup = function(a) {
	K = a.type[7] && a.button + 1;
},
onmousemove = function(a) {
	K ? j += L - a.clientX : 0,
	L = a.clientX
},

setInterval(function () {
    h++, // Frame count
	N = top.iter || N, // Num iterations/recursion depth
	t = new Date/1e3 - j/100,

	// Some oscillators
	k = Math.sin(t/13)/2 + .5,
	l = Math.sin(t/17)/2 + .5,

	// This is the precalcs that help with the trig avoidance later on.
	m = Math.sin(t/=10), q = Math.cos(t),
	o = Math.sin(t/3), s = Math.cos(t/3),
	n = Math.sin(t/=2), r = Math.cos(t),
	p = Math.sin(t/=2), u = Math.cos(t),

	// Run the function, resetting particle count (i) and F which is set if the iteration count just changed.
	F = f(i = 0, 0, 0, W, H)
}, F = 1),

c.fillRect(K=0, 0, W = a.width = innerWidth, H = a.height = innerHeight), // Clear to black initially.

// Fade out, if we do this less than every frame it's faster and we can fine tune the params.
setInterval(function () {
    c.fillStyle = "rgba(0,0,0,.05)", c.fillRect(0, 0, W, H)
}, 36),

// Check/reset frame count every second then adjust iterations depending on it.
// Less than 20 FPS = less iters, more than 140 = more iters.
setInterval(function () {
    F = 20 > h ? N-- : h > 140 ? N++ : F, h = 0
}, 1e3)