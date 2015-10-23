var sin = Math.sin;
var cos = Math.cos;

var w = 680; // Width
var h = 510; // Height
var cw = w * 0.5; // Center width
var ch = h * 0.5; // Center height

var hs = 10; // Heart size
var or = 80; // Outer radius, determines offset to roll inner circle around
var ir = 40; // Inner radius of circle rolled around heart shape
var irt = ir; // Inner radius target, to interpolate/morph between shapes
var d = 0; // Distance of point from center of inner radius
var dt = d; // Distance target, to interpolate/morph between shapes
var t = 0; // Theta
var px, py; // Previous x & y coordinates of last drawing position
var hue = 0;

function lerp(p, a, b) {
	return a + (b - a) * p;
}

function onMove(event) {
	// Calculate new radius and distance values on mouse location
	irt = lerp(event.clientX / w, 38, 42);
	dt = lerp(event.clientY / h, -80, 80);
}

function onClick(event) {
	// Random outer circle radius changes the 'frequency' of the lines
	or = Math.random() * 160;
}

function draw() {
	t = 0;
	px = py = undefined;
	
	// Interpolate towards target values slowly
	ir += (irt - ir) * 0.02;
	d += (dt - d) * 0.04;
	hue = (ir - 40) * d;
	
	// Clear canvas with a translucent colour to create 'blur'
	a.globalAlpha = 0.2;
	a.globalCompositeOperation = "source-over";
	a.fillRect(0, 19, w, h);
	
	// Set composite so colours add together when overlayed
	a.globalAlpha = 0.5;
	a.globalCompositeOperation = "lighter";
	a.beginPath();
	
	for (i = 0; i < 1200; i++) {
		t += 0.1;
		
		// Calculate point along heart shape
		x = cw + hs * (16 * (Math.pow(sin(t), 3)));
		y = (ch - 20) + hs * -(13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t));
		
		// Calculate point on a circle rolled around the heart shape
		x += d * cos(((or - ir) / ir) * t);
		y -= d * sin(((or - ir) / ir) * t);
		
		a.moveTo(px, py);
		a.lineTo(x, y);
		px = x;
		py = y;
	}
	
	a.strokeStyle = "hsl("+hue+",60%,50%)";
	a.stroke();
	a.closePath();
}

c.width = w;
c.height = h;

// Text at the top of the canvas
a.fillRect(0, 0, w, h);
a.fillStyle = "#999";
a.font = "13px sans-serif";
a.fillText("Click for a different kind of love", cw - 89, 14);
a.fillStyle = "#000";

c.onmousemove = onMove;
c.onclick = onClick;

// Start the render loop (approx 60fps)
setInterval(draw, 16);