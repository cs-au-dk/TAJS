// Jim Bumgardner krazydad.com
c.width = c.height = 1024;
var nd = 1024;
var w = c.width;
var h = c.height;
var fc=Math.floor(Math.random()*100000);
var p2 = 3.1415927*2;
setInterval(function() {
	a.fillStyle = 'rgba(0,0,0,.05)';
	a.fillRect(0,0,w,h);
	a.fillStyle = '#fff';

	var n = fc/1024;
	var rd = w*.97/2;
	var cx = w/2;
	var cy = h/2;
	for (var i = 0; i <= nd; ++i) {
		var t = i*p2/nd;
		var r = rd*Math.sin(n*t);
		var r2 = 1+Math.abs(r/70);
		var px = cx+Math.cos(t)*r;
		var py = cy+Math.sin(t)*r;
		var hu = Math.pow(i/nd,2)*360;
		a.fillStyle = 'hsla(' + hu +',33%,66%,1)';
		a.beginPath();
		a.arc(px,py,r2,0,p2,false);
		a.fill();
	}
	++fc;
}, 40);