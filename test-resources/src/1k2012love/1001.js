// sorry, no comment except this one as I "compressed" my code myself and cutted them at first :P
// this version I used to reduce it is still "readeable" if are motivated :P
d=600
c.width=d
b.style.backgroundColor = '#000'
S=c.style
S.width=window.innerWidth+'px'
S.height=window.innerHeight+'px'
X=0
Y=0
u=Math
q=1e4
v=u.random
w=u.abs
h=10
H=.1
b.addEventListener("mousemove", function(e){X=e.clientX;Y=e.clientY})
r = function()
{
	c.height = d
	z = a.getImageData(0, 0, d, d)
	for (i=0;i<$.length;++i)
	{
		C=$[i]
		C.m()
		g = gr((w(C.x-X)+w(C.y-Y))/h,{r:C.x/q,g:C.y/q,b:H},{r:H,g:H,b:H},99,255)
		o(z, C.x, C.y, g.r, g.g, g.b,C.br+v()*99,4)
	}
	a.putImageData(z,0,0)
	setTimeout(r,16)
}

s=function(i,f,p,w,c){return u.sin(f*i+p)*w+c}
gr=function(i,f,p,c,w){return{r:s(i,f.r,p.r,w,c),g:s(i,f.g,p.g,w,c),b:s(i,f.b,p.b,w,c)}}

f = function(x,y)
{
	t=this
	t.br = v()*156
	t.x = x
	t.y = y
	t.m = function()
	{
		t=this
		g = s(w(X-t.x+Y-t.y),.007,1,h,h)
		t.x += g/h + X /99
		t.y += g/h + Y /99
		t.x > d + 1 && (t.x = -1)
		t.y > d + 1 && (t.y = -1)
	}
}
$ = []
for (i=0;i<d;i+=h)
{
	for (j=0;j<d;j+=h)
	{
		$.push(new f(i,j))
	}
}

o = function(z, x, y, r, g, b, a,s) {
	for (m=0;m<s;++m)
	{
		for (l = 0; l < s; ++l) {
//				if((x<0) || (x>d) || (y<0) || (y>d)) return
				k = ((y+l>>0) * z.width + (x+m>>0)) * 4
				_=z.data
				_[k] = r
				_[k+1] = g
				_[k+2] = b
				_[k+3] = a
		}
	}
}
r()