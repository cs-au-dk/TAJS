"use strict";

var i,l,k,e,s,g,h,o,r,w,d,n;

i=620;
l=i/2;
k=35;
e=[];
s=250;
g=h=o=0;
r=Math.random;
w=Math.sqrt;
d=Math.abs;
b.onmousemove=function(f)
{
	g=f.clientX;
	h=f.clientY
};
b.onmousedown=function(){o=1};
b.onmouseup=function(){o=0};
c.width=c.height=i;
for(n=s;n--;)
{
	e[n]=
	{
		x:i*r(),
		y:-i*r(),
		e:5,
		c:0,
		d:0,
		a:0,
		b:0,
		f:"#5D0"
	};
}
setInterval(
	function()
	{
		var f,j,m,p,E,A,B,t,q,u,v,z;
		for(E=k;E--;)
		{
			for(m=s;m--;)
			{
				f=e[m];
				A=f.x;
				B=f.y;
				t=f.e;
				q=B-.9*i+t;
				0<q&&(f.b-=6*q);		// Walls.
				q=5-A+t;
				0<q&&(f.a+=6*q);
				q=5+A-i+t;
				0<q&&(f.a-=6*q);
				for(p=m;p--;)
				{
					j=e[p];
					u=A-j.x;
					v=B-j.y;
					q=t+j.e-0.85*w(u*u+v*v);
					if(0<q)						// Check for collisions
					{
						v/=d(u);				// Trig identies.
						u=u/d(u);
						u=-u/w(1+v*v);
						v=w(1-u*u)*v/d(v);
						z=20*q					// Hooke's Law.
						z=z+(f.c-j.c)*u/3;		// Viscous damping.
						z=z-(f.d-j.d)*v/3;
						f.a-=z*u;
						f.b+=z*v;
						j.a+=z*u;
						j.b-=z*v;
					}
				}
			}
			for(m=s;m--;)			// Calculus.
			{
				f=e[m];
				f.c+=f.a/k;
				f.d+=f.b/k+0.35/k;
				f.x+=f.c/k;
				f.y+=f.d/k;
				f.a=0;
				f.b=0;
			}
		}
		a.fillStyle="#000";
		a.fillRect(0,0,i,i);
		a.strokeStyle="#0AF";
		a.moveTo(g,h);
		a.lineTo(l,l);
		a.stroke();
		for(m=s;m--;)				// Painting.
		{
			f=e[m];
			a.fillStyle=f.f;
			a.beginPath();
			a.arc(f.x,f.y,f.e,0,6);
			a.fill();
		}
		if(o)						// Fire, if necessary.
		{
			k=Math.round(9E3/s);
			e[s++]=
			{
				x:g,
				y:h,
				e:4,
				c:(l-g)/6,
				d:(l-h)/6,
				a:0,
				b:0,
				f:"#05F"
			}
		}
	}
,15);