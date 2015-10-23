C=document.createElement("canvas");S=C.style;S.position="relative";A=C.getContext("2d");b.appendChild(C);//add extra canvas for the ant
w=C.width=C.height=c.width=c.height=400;
with(M=Math)R=random,q=P=PI,G=cos,F=floor;
S.top=0+'px';S.left=-w+'px';
L=P/4;
s=n=f=false;
T=setTimeout;
r=5;g=195;e=320;k=100;l=50;X=o=p=0;z=10;
function B(x,y,r,a){//draw a circle
	a.beginPath();
	a.arc(x,y,r,P*2,0,f);
	a.fillStyle='#'+F(R()*z)+'8'+F(R()*z);
	a.fill();
	a.stroke()
}
A.translate(190,e);O(0,0,0);
function O(a,U,V){//draws the ant
	A.translate(U,V);
	A.rotate(a);
	for(i=1;i<4;i++){
		t=i*r*3;
		//the legs
		A.beginPath();
		A.arc(X,r-25-t,30,L,3*L,f);
		A.stroke();
		//the body
		B(X,r-t,z,A)
	}
	//the eyes
	B(-4,-45,3,A);
	B(4,-45,3,A);
	A.rotate(-a)
}
function H(z){
	//http://mathworld.wolfram.com/HeartCurve.html
//thanks to Love trails By Rauri Rochford for the link
	x=(20+(16*M.pow(M.sin(q),3)))*z;
	y=(15-(13*G(q)-5*G(q*2)-2*G(3*q)-G(4*q)))*z;
	//draw the flower leaves as circles
	h=[1,-1,0,0];
	for(i=0;i<4;i++){
		B(x+h[i]*r,y+h[3-i]*r,r,a)
	}
	//add the flower heart
	B(x,y,r/1.3,a);
	m=M.atan2(l-y,k-x);//find the tangent angle to the heart curve
	if(!n){//the ant only walks the first tour
		A.clearRect(-g,-g,w,w);
		O(-(p),(-g+x),(-e+y))
	}
	p+=(o-m)%(L);
	o=m;
	k=g=x;
	l=e=y;
	if(!s&q>0){//do the right half
	q-=.1;
	T("H(z)",170)
	}else if(!n&q>-P){//do the left half
		if(!s)p+=-P/1.5;
		s=!f;
		q-=.1;
		T("H(z)",170)
	}else{//keep the flowers looping
		n=s;
		q-=.3;
		T("H(z)",6)
	}
}
T("H(z)",170);
c.style.background='#234';