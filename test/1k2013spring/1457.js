// Hand minified version, partially optimized for JS Crush
with(Math){
	L=[];
	D=5;
	r=random;
	c.width=c.height=S=window.innerHeight;
	a.font='bold 2em arial';
	for(m in a)
		a[m[W=T=N=0]+(m[6]||'')]=a[m]

	A=function(T,x,y,c,r,C){
		a.fillStyle=T[4]?(
			G=a.cR(T[2],T[3],T[0],T[2],T[3],T[1]),
			G.addColorStop(0,c),
			G.addColorStop(1,T[4]),
			C&&G.addColorStop(.1,C),
			G):c||'#000';
		a.ba();
		a.m(x,T?y:y-40);
		for(n=0;(T||1)>n++;)
			T?a.bC(r*sin(2*PI/T*(n+1))+x,r*cos(2*PI/T*(n+1))+y,r*sin(2*PI/T*n)+x,r*cos(2*PI/T*n)+y,x+n%2,y):a.l(x+15,y)||a.arc(x,y,15,0,PI)
		a.ca();
		1/T||a.arc(x,y,r,0,2*PI);
		r?a.fill():a.fx(T[0],x,y);
	}

	setInterval(
		function(){
		for(a.ce(0,0,S,S),
			u=(T++*S/1080+2*S/3)%(5*S/3)-S/3,
			U=9/(4*S)*pow(u-S/3,2),
			A([S/10,S,u,U,'hsl('+(180+T/5)+',30%,50%)'],u,U,'#FF0',S*3,'hsl('+(180+T/5)+',100%,80%)'),
			A([S/3,S,u,S-U,'#120'],S/3,5*S/3,'#2F3',S),
			A([W],15,40),
			i=0;l=L[i++];)
				l.r?A(l.p,l.b+=.1,l.f+=.2,l.c,l.r):(A(0,l.b+10,(l.f+=W/15+l.e)-10,'#3AB',1),A(l.a.toString(36),l.b,l.d||l.f,l.d&&'#F00'))||!l.d&&l.f>S-15&&(l.d=S-15,D&&D--);
			D?(!(T%80)||N)&&L.push({
				a:0|r()*36,
				b:.9*r()*S,
				f:N=0,
				e:2*r()+2
			}):A(['GAME OVER'],S/3,S/3)
	}
	,16);


	onkeydown=function(e){
		for(k=e.keyCode-48,i=0;l=L[i++];)
			l.d||l.a!==k-(k>9&&7)||
				(l.p=0|9*r(l.a="")+5,
					l.r=60*r(i=1e9)+15,
					l.c="hsl("+360*r()+',100%,80%)',
						D&&W++%2&&N++)
	}
}