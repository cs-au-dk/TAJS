// Shorthand names
m =Math;
r = m.random;
g = Date;
e = document;
q = e.createElement('canvas');
w = e.createElement('canvas');
v = window;

// Variables
o = {};
h = 100;
H = 200;
l = +new g;

// Store and set area dimensions
t = c.width = v.innerWidth,
u = c.height = v.innerHeight-5;

// Function that renders a pixel and a circle to whatever canvas 'a' points to.
function d(c,x,y,s){
	a.fillStyle = c;
	
	a.beginPath();
	a.arc(x,y,s,0,m.PI*2,true);
	a.fill();
	
	a.fillRect(x,y,1,1);
}

// Setup document body
b.setAttribute('style', 'margin:0;background:#000');

// Store reference to main canvas. d() always renders to a.
k = a;

// Render a heart to buffer & hills to buffer
q.width = q.height = h;
w.width = 1000; w.height = H;

// Bigg messy loop doing all initialization
for (j = 0; ++j < H;) {
	for (i = 0; ++i < 1000;) {
		// Work on the heart
		a = q.getContext('2d');
		z = 0.5-i/h;
		f = ((j/h-0.5)+0.4*m.sqrt(m.abs(z)));
		f = z*z+2*f*f;
		if (f < 0.23) 
			d(f>0.16?'#F00':'#F88',i,j,0);
		
		// Work on the hills
		a = w.getContext('2d');
		d(
			j>0.0005*i*i-0.3*i+h ? "#343" : 
			j>0.0004*i*i-0.9*i+500 ? "#232" : '#000',
		i,j,0);
	}
	
	// Init hearts and stars array
	o[j] = {x: r()*t, y:-h-r()*u, z:51-j/4, s:25+j*0.4};
	o[H+j] = {x: r()*t, y:r()*u-H, s:r()*3+1, p:j}
}

// Restore reference to main canvas
a = k;

// Render & logic callback
v.setInterval(function() {
	n=+new g;
	// Clear
	a.clearRect(0, 0, t, u);
	
	// Moon
	d("#FFA", H, 250, 150);
	d("#000", 270, 320, h);
	
	// Hills
	a.drawImage(w, 0, u-H, t, H);
	
	for(i=0;++i<H;) {
		// Time since last frame
		f = (n-l) / h;
		
		// Star render and update
		s = o[H+i];
		d("#FFA", s.x, s.y, m.floor(m.max(s.s + m.sin(++s.p / 10) - 0.5, 1)));
		
		// Heart render and update
		z = o[i];
		a.drawImage(q, z.x+=(-0.1/(z.z/h))*f, z.y+=(5-(z.z/10))*f, z.s, z.s);
		
		// Left and bottom bounderies
		if(z.y > u) 
			z.y = -h;
		if(z.x<-H) 
			z.x = t;
	}
	l = n;
}, 60);