c.setAttribute('width', '400');
c.setAttribute('height', '400');
b.style.backgroundColor = '#FF1493';

var M = Math;
function ra() {
	return M.floor(M.random() * 255);
}
function cc() {
	return 'rgb(' + ra() + ', ' + ra() + ', ' + ra() + ')';
}
var h = '#FF69B4';
a.b = a.bezierCurveTo;
a.l = a.lineTo;
a.cp = a.closePath;
a.bp = a.beginPath;

a.scale(1.8, 1.8);
x = 85;
y = 70;


a.strokeStyle = '#FF69B4';

var il = true;
a.lineWidth = 2;
var o = cc(); // get the first random color
var ii = setInterval(
	function() {
		var lm = il ? r: l;
		il = !il;
		oo = cc(); // get the next random color
		
		a.fillStyle = oo;
		lm(x, y, 0);
		a.fill();
		
		l(x, y, 1);
		a.stroke();
		if (o == oo) clearInterval(ii);
	}, 300
);

var e = 30, f = 25, g = 35, h = 80; // minify!
function l(x, y, b) {
	a.bp();
	a.b(x + f, y + f, x + 20, y, x, y);
	a.b(x - e, y, x - e, y + g,x - e,y + g);
	a.b(x - e, y + 55, x - 10, y + 77, x + f, y + 95);
	if (b == 1) {
		a.b(x + 60, y + 77, x + h, y + 55, x + h, y + g);
		a.b(x + 80, y + g, x + h, y, x + 50, y);
		a.b(x + g, y, x + f, y + f, x + f, y + f);
	} else {
		a.l(x + e, y + h);
		a.l(x + 20, y + 60);
		a.l(x + e, y + 40);
	}
	a.cp();
}

function r(x, y, b) { // b is useless
	a.bp();
	a.l(x + e, y + 40);
	a.l(x + 20, y + 60);
	a.l(x + e, y + h);
	a.l(x + f, y + 95);
	a.b(x + 60, y + 77, x + h, y + 55, x + h, y + g);
	a.b(x + h, y + g, x + h, y, x + 50, y);
	a.b(x + g, y, x + f, y + f, x + f, y + f);
	a.cp();
}