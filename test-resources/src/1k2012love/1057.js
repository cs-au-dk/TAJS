with(Math) {
	
	s=12, f=z=40, n=[], t=setTimeout;
	c.width = c.height = w = s*z;
	with(b.style) margin = padding = 0;
	c.style.border = '1px solid red';
	
	v = function() { return random() < .3 }
	
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
	
	c.onclick = function(e) { f=0; q(floor(e.pageX/s), ceil(e.pageY/s)) };
	
	(l = function() {
		with(a) {
			clearRect(0,0,w,w),font = s+2+'px a', save()
			for(x in n) for(y in n[x]) fillStyle = 'rgba(255,26,0,'+(n[x][y]-=.1)+')', fillText('♥',s*x,s*y)
			if(f) fillStyle = 'red', fillText('CLICK 4 LOVE',20,30), restore()
		}
		t(l,20)
	})()
	
}