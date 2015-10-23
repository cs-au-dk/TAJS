function G (gr, sr, cr, rs) {
	// a coordinate class
	function C (x, y) {
		this.x = x;
		this.y = y;
	}

	// polar to cartesian conversion
	function PC (c, r, a) {
		var t = new C (r * Math.cos (a), r * Math.sin (a));
		return new C (t.x + c.x, t.y + c.y);
	}

	// calculate control point location
	function GCP (gc, gr, sr, cr, ra, ca) {
		return PC (PC (gc, gr - sr, ra), cr, ca);
	}

	oc.clearRect (0, 0, w, h);
	var dr = 0.017453292519943;
	var gc = new C (w / 2, h / 2);
	var ca = 0;
	var ra = 0;
	var as = 1;
	var cs = as * (1.0 / rs);
	var p = GCP (gc, gr, sr, cr, ra, ca);
	oc.beginPath ();
	oc.moveTo (p.x, p);
	for (i = 0; i <= 360 * (1 / cs); i++) {
		p = GCP (gc, gr, sr, cr, ra, ca);
		oc.lineTo (p.x, p.y);
		ra -= as * dr;
		ca -= cs * dr;
	}

	oc.stroke ();
	c.clearRect (0, 0, w, h);
	c.drawImage (ocn, 0, 0, w, h);
	return true;
}

// draw current frame
function D () {
	G (gr, sr, gr / 3, r);
	r = r + s;
	d = r < 20 + s;
	if (d) {					
		requestAnimationFrame (D);
	}
}

window.onclick = function () {
	// random integer between two numbers
	function R (f, t) {
		return Math.floor ((Math.random () * (t - f + 1)) + f); 
	}
	
	r = 0.1;
	gr = R (w / 20, w / 6);
	sr = R (w / 40, w / 3);
	if (!d) {
		D ();
	}
};

var w = a.width;
var h = a.height;
var d = 0;
var gr = w / 5;
var sr = gr / 4;
var s = 0.05;
var r = 0.1;

// initialize offscreen context
var ocn = document.createElement ('canvas');
ocn.width = w;
ocn.height = h;
var oc = ocn.getContext ('2d'); 	
D ();