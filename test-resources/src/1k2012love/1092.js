with(Math) {
	
	s=15, z=50; n=[], t=setTimeout;
	c.width = c.height = w = s*z;

	txt = ["Love","Liebe","Amor","Cinta","♥"], img = [];
	colors = ["255,26,0", "255,255,0", "26,255,26"];
	
	tl = txt.length;
	cl = colors.length;
	
	a.font = "240px Georgia";
	a.textBaseline = "middle";
	a.textAlign = "center";
	
	for (var j = 0; j < tl; j++)
	{
		// Draw text
		a.fillStyle = "#f00";
		a.fillText(txt[j],c.width/2,c.height/2);
	
		// Save image data
		img[j] = a.getImageData(0,0,c.width,c.height);
		
		// Clear the canvas
		a.clearRect(0,0,c.width,c.height);
	}


	with(b.style) margin = padding = 0;
	c.style.border = '0px';
	
	fl = .3;
	v = function() { return random() < fl }
	
	function p(k,l) { if(v()) q(k,l) }
	
	function u(k,l) {
		
		t(function() {
			m=k-1; d=k+1; o=l-1; j=l+1;
			p(m,o); p(m,l); p(m,j);
			p(k,o); p(k,j);
			p(d,o); p(d,l); p(d,j);
		},30);
		
	}
	
	function q(k,l) {
		if(n[k] && n[k][l] <= -1) {
			n[k][l] = 1;
			u(k,l)
		}
	}
	
	for(n=[],x=z; x--;) for(n[x]=[],y=z; y--;) n[x][y] = -1;

	hf = function(n){
		return Math.floor(Math.random()*n);
	};
	
	tr = function() {
		h=hf(tl);
		for (var y = 0; y < img[h].height; y+=s)
		{
			for (var x = 0; x < img[h].width; x+=s)
			{	
				var k = (y*img[h].width*4) + (x*4);
				if (img[h].data[k] !== 0)
				{
					n[floor(x/s)][ceil(y/s)] = 1;
					u(floor(x/s),ceil(y/s));
				}
				
			}
		}
	}
	
	c.onclick = function(e) { 
		tr();
	};
	
	yu = 10;
	var counter = 0;
	var red = true;
	(l = function() {
		//if (yu--) yu=10,g();
		// else
		flv = random();
			counter++;
			if (counter > 8) counter = 0, red = !red, tr();
			 else
		with(a) {
			clearRect(0,0,w,w), font = s+2+'px a';//, save();
			for(x in n) for(y in n[x]) {
				red ? clrs = colors[hf(cl)] : clrs = colors[0];
				fillStyle = 'rgba('+clrs+','+(n[x][y]-=.1)+')', fillText('♥',s*x,s*y)
			}
			//if(f) fillStyle = 'red', fillText('CLICK 4 LOVE',20,30), restore()
		}
		t(l,20)
	})()
	
	
}