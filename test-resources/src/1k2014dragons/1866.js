// Setup
	w = a.width;
	h = a.height;
	m = 150;
	n = 50;
	f = [];
	t = 0;
	// The dragon image.  Wrote a separte page to load a dragon image (over 1kb) onto a canvas, 
	// then read the pixels and encoded as ascii chars (32-126).  This is broken up into 4 blocks of
	// 23 where a block represents a transparency level (fully transparent -> black) and the count in that
	// block is the count of that transparancy "colour".
	d = "{{gO{qO9OvOiOoO#sP:gPp#On\"9%O8!fPm8$Om8,8nO%Oj8-8p8&8i,Or'hO*Os8(Of8$9Ox.O{g8*{iP8(OiO{f!P(9\"8OxPf8#8!S8OpSm#8{gO8OjO!O{k9i!8{mO!P9{oO8!8{";
	
	// Abbreviate context methods
	for($ in c)
		c[$[0]+($[6]||'')]=c[$];
	
	// Uncompress dragon image string into an array of bytes
	r = [];
	r.push();
	for(i = 0; i < d.length; i++) r.push(d.charCodeAt(i));
	
	// Rotation of canvas to give the "angle view" of the rain drops
	b.style.background = "#000";
    with(a.style) webkitTransform = MozTransform = msTransform = "rotateX(65deg)";
	
	R = Math;
	
	// Function to return a random number up to d
	A = function(d){
		return R.random()*d|0;
	};
	
	// Initialise circles
	for(i = 0; i < m; i++)
		f[i]={x:A(w),y:A(h),r:0,t:A(n)}
	
	// Create the background colour gradient
	g = c.cR((w*.4)|0,(h*.2)|0,0,(w*.4)|0,(h*.2)|0,(w*.4)|0);
	g.addColorStop(0,"#FCD647");
	g.addColorStop(1,"#F58C21");

	// Animation frame
	T = function(d){
		
		// Fill background with gradient
		c.fillStyle = g;
		c.fc(0, 0, w, h);

		/* Draw the dragon, which is represented as counts of a colour or transparency.
		   Include a Sin offset in the y direction to simulate crude motion and a linear gradient
		   for the first pixel when transitioning between colours (poor man's anti-aliasing effect).
		*/
		p = 0;
		k = "rgba(0,0,0,0)";
		var d = (1 - (R.abs(w*.3 - t++*2) / (w*.3)));
		for(i = 0; i < r.length; i++){
			o = (r[i] - 32) % 23;
			col = ((r[i] - 32 - o) / 23) | 0;
			F = d * (3 - col) / 10;
			e = "rgba(0,0,0," + F + ")";

			for(l=0;l<o;l++)
				x = t*2 + (p % 37) * 8,	
				y = h/2 - t + (p++ / 37) * 8  + R.sin(x/20) * 5,
				u = c.cL(x, y, x + 8, y),		 // Create Linear Gradient
				u.addColorStop(0,k),
				u.addColorStop(1,e),
				c.fillStyle = l == 0 ? u : e,
				c.fc(x, y, 8, 8);
			k = e;
		}
	
		// Draw circles, these use RGBA colours to include transparency, circles are filled with a border
		for(i = 0; i < m; i++){
			o = f[i];
			
			if (o.t == 0)f[i] = {x:A(w),y:A(h),r:A(m),t:n}

			l = "rgba(255,255,0,";
			e = o.t/n;
			F = o.r * (1 - e);
			if (F < 1)F = 1;
			
			c.ba();
			c.a(o.x, o.y, F, 0, 6.3, 0);
			c.fillStyle = l + e/10 + ")";
			c.fill();
			c.ld = (n - o.t--) / 10;
			c.strokeStyle=l+e/6 + ")";
			c.s();
		}
		
		// Next frame
		setTimeout(T, 20);
	}
	
	// Start animating
	T();