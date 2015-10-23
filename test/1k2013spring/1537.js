k = 0
w=window
h=w.innerHeight
w=w.innerWidth
h2=h/2
W=40
H=40

setInterval(function(){
	c.width=w
	c.height=h
	o = 0;
	
	p = []	
	for(j = 0; j < H; j++) {
		for(i = 0; i < W; i++) {
			x = 1.2 * i * w / W +8 * Math.sin((j + k) * 0.15)
			y = 1.2 * j * h / H + 8 * Math.sin((i + k) * 0.3)
			z = 512 + 32*Math.sin((i+j+k) * 0.2)
			
			p[o] = (512 * x) / z
			p[o+1] = (512 * y) / z
			p[o+2] = z;
			o+=3
		}
	}
	o = 0

	for(j = 0; j < H-4; j++) {	
		for(i = 0; i < W-1; i++) {
			l = []
			l[0] = p[o+3] - p[o]
			l[1] = p[o+4] - p[o+1]
			l[2] = p[o+5] - p[o+2]
			
			m = []
			m[0] = p[o+W*3] - p[o]
			m[1] = p[o+W*3+1] - p[o+1]
			m[2] = p[o+W*3+2] - p[o+2]
			
			n =[]
			n[0] = l[1]*m[2] - l[2]*m[1]
			n[1] = l[2]*m[0] - l[0]*m[2]
			n[2] = l[0]*m[1] - l[1]*m[0]
			
			n = n[2] / Math.sqrt(n[0] * n[0] + n[1] * n[1] + n[2] * n[2]);
			n *= n;
			
			g = 221
			b = 9
			if(((j / 4) % 2|0) == 0){
				r = 253
			} else {
				r = g
				g = b
			}	
			
			r=r*n|0
			g=g*n|0
			b=b*n|0
			
			a.fillStyle="rgba("+r+","+g+","+b+",1)"
			a.strokeStyle=a.fillStyle
		
			a.beginPath()
			a.lineTo(p[o], p[o + 1])
			a.lineTo(p[o+3], p[o + 4])
			a.lineTo(p[o + 3 + W*3], p[o + 4 + W*3])
			a.lineTo(p[o + W*3], p[o + 1 + W*3])
			a.fill()
			a.stroke()
			o+=3
		}
		o+=3		
	}
	
	w4=4*w/6
	w1=w/10
	cols=["#5E96FF","#387DFF","#0F47AF","#0D3E99","#0B347F","#08265B"]
	for(j = 0; j < 32; j++) {
		for(i = 0; i< 6;i++) {
			a.fillStyle=cols[i]
			a.strokeStyle=a.fillStyle
			wb=w4 / 32
			hb=w1 / 6
			x=j * wb
			y=h2 + (i-3) *hb - hb*Math.sin(k*0.2 + j *0.6)
			a.fillRect(x,y,wb,hb)
			a.strokeRect(x,y,wb,hb)
		}
	}
	
	a.fillStyle="#ffffff"
	a.strokeStyle=a.fillStyle
	a.beginPath()
	for(i = 0; i < 5; i++) {
		s = Math.sin(k * 0.2)*32
		l = w1 * Math.sin(i* 1.256) + s/4
		m = w1 * -Math.sin(i* 1.256 + 1.57) + s
		n = w/25 * Math.sin(i* 1.256 + 0.628)
		o = w/25 * -Math.sin(i* 1.256 + 2.19) + s
		a.lineTo(w4 + l, h2 + m)
		a.lineTo(w4 + n, h2 + o)
	}
	a.fill()
	a.stroke()
	k+=0.5
},20);