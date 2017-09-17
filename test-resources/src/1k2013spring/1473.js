with(Math) R=random, F=floor, S=sin, C=cos, Q= PI*2;
for(Z in a)a[Z[0]+(Z[6]||Z[2])]=a[Z]; //canvas context hash trick http://www.claudiocc.com/javascript-golfing/
//X=angle of a sector
//d= direction of the player
//p= position of the player
//g= global angle of the hexagon
//h= global hue
//q=sectors queue (each sector is [sector#, radius, type])
//l= level
W=600,X=1.047,d=p=g=l=h=0,q=[],v=o=A=Z=1;

c.width= c.height= W,

setInterval(function()
{
	if (Z)
	{
	//background
	K(l%2?1:8.5); a.fc(0,0,W,W); //cls

	a.sv();	
	v+= R()*.1-.05;
	v= v<.9?.9:(v>1.4?1.4:v);
	a.ta(W/2, W/2); //translate
	a.sa(v, v+S(g)*.3); //scale
	
	K(l%2?1.3:9); 
	P(1, 40, 0)
	P(3, 40, 0)
	P(5, 40, 0)
	
	K(5);
	for(i=6;i--;) P(i, 40, -1);

	//player
	p+= d; if (p<0) p=Q+p; if (p>Q) p= p-Q;
	a.sv();
	a.ta(C(g+p)*48, S(g+p)*48);
	a.rt(g+p);
	a.fx('▶',-3,4);
	a.re();

	//sectors
	for (i=0; s=q[i]; i++) if(s^1)
	{
		P(s[0], s[1], s[2]);
		s[1]-= 4+l*.7;
		(s[1]< 40-s[2]*20 && (q[i]=1)) || 
		(s[1]<52 && p> s[0]*X && p< (s[0]+1)*X && (Z=0)	)
	}

	a.re();
	K(5);
	a.sv();
	a.sa(3,3);
	a.fx('L'+l,2,10);
	a.fx(o,88,10);
	a.re();

	//other
	g+=(.015+l*.004)*A;
	if (R()<.003) A*=-1;
	if (o%F((6-l/2)*10)==0) //add sector
	{ 
		//n= F(R()*3)+(l>3?3:1);
		n= 2+F(R()*4);
		//(each sector is [sector#, radius, type])
		m= F(R()*6); //start sector
		for(;n--;)
			x= F(1+R()*l), 
			z= R()<.5||n>4?1:2,
			m= (m+z)%6,
			q.push([m, W, x]);
		
	}
	h+=h>W?-h:.8;
	!--o && (l++, o=W);
	//if (l>6) a.fx('WIN!',W2,99), Z=0; //no level limit (was 6)
	}

}, 20)

P= function(s, r, t)  //paints a section of the hexagon. s: section, r: radius, t: type
{
	s= s*X+g;
	var w= t==0?W:t*20;
	/*if (t==-1) w= r+3;
	else {
		w+= r;
		if (r<40) r= 40
	}*/
	(t==-1&&(w=r+3))||(w+= r, r=r<40?40:r);
	t= s+X;
	a.ba();
	a.ln(C(s)*r, S(s)*r);
	a.ln(C(t)*r, S(t)*r);
	a.ln(C(t)*w, S(t)*w);
	a.ln(C(s)*w, S(s)*w);
	a.fl()
}
K=function(b) //constructs a color. hue, brightness
{
	a.fillStyle= 'hsl('+h+',100%,'+(b*10)+'%)'
}
b.onkeydown=function(f){k=f.which;d=k==37?-.15:(k==39?.15:0) } //37 <-> 39
b.onkeyup=function(){d=0}