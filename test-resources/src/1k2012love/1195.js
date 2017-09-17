c.width= W= c.height= 300;
R= Math.random;
n= 'px courier';
E=B=C=[];	
L= 0;
w= W/2;
X= Y= w;
K=[];
t=.2;
v=20;
P=0;
for(i=0; i<6; i++) C[i]= {x:v+i*(40+R()*v), h:v};
D= document;
D.onkeydown= D.onkeyup= function(ev){K[ev.keyCode]= ev.type!='keyup'};
a.lineWidth= .2;
iv= setInterval(function(){
	c.width= W;
	F(0,0,W,W);
	a.strokeStyle='#59f';
	a.beginPath();
	for (e=0;e<E.length;e++) {
		u=E[e];
		a.moveTo(u.s, 0);
		a.lineTo(u.s+u.e*(u.l/W), u.l);
		u.l+=t;
		if (u.l>W) 
		{
			for (i in C) 
				if (Math.abs((u.s+u.e)-(C[i].x+10))<10){ C[i].h-=3; }
			E.splice(e,1); e--;
		}
	}
	a.stroke();
	if (E.length<v && R()<.02) {
		E.push({s:R()*W, e: (R()*W-w)/2, l:0});
		t+=t<1?.005:0
	}
	for (b=0; b<B.length;b++){
		u=B[b];
		G('rgba(255,70,70,.5)');
		a.font= u.z+n;
		a.fillText('♥', u.x-u.z/2, u.y+u.z/2);
		u.z++;
		if (u.z>100) B.splice(b,1), b--;

		for (e=0;e<E.length;e++){
			dx= u.x-(E[e].s+E[e].e*(E[e].l/W)), dy=u.y-E[e].l;
			if (dx*dx+dy*dy < u.z*u.z*.15)
				E.splice(e,1), e--, 
				P+= Math.floor((110-u.z)/5)*5;
		}
	}
	X+=K[39]?2:(K[37]?-2:0);
	Y+=K[40]?2:(K[38]?-2:0);
	if (K[32] && !L) q= X, Q= Y, L= .01;
	if (L>0){
		G('#fff');
		F(w+(q-w)*L, W+(Q-W)*L, 2,2);
		L+=.04;
		if (L>=1) {B.push({x:q, y: Q, z:1}); L=0}
	}
	G('#c33');
	for (i in C) F(C[i].x, W, v, -C[i].h);
	F(X, Y, 2, 2);
	a.font= 16+n;
	a.fillText(P, 9, v);
}, v/2);
function F(x,y,w,h){a.fillRect(x,y,w,h)}
function G(c){a.fillStyle=c}