bP=function(x,y){	// begins a canvas path - helper function
	c.beginPath();
	c.strokeStyle=x;
	c.fillStyle=y;
},
eP=function(){	// ands, strokes and fills a canvas path - helper function
	c.closePath();
	c.stroke();
	c.fill();
},
arc=function(x,y,z,h,i,j){	// draws a canvas arc -helper function
	bP(i,j);
	c.arc(x,y,z,0,h);
	eP();
},
pw=function(x){return m.pow(x,2)},
D=function(){return (new Date()).valueOf()},
rn=function(){	// main game loop function for drawing and all handling (saves space) 
	bP(c0,c0);
	c.fillRect(0,0,wd,ht);	// refresh background
	cl=(mC)?c2:c3;
	arc(cX,cY,sz,r2,cl,cl);	// snow ball is white while shaking, white point in the mid
	if(!mC){ 
			for(i=0;i<mx;i++){	// draw snow
				o=sw[i];
				if(o!=nl){
					sC=sp/z;
					x=o.x-cX;
					x=x>0?x-sC:x+sC;
					y=o.y-cY;
					y=y>0?y-sC:y+sC;
					r=sq(pw(x)+pw(y));
					if(r+o.s<=5) sw[i]=nl/*,nlCnt++*/;
					else{
						r=y<0?r*-1:r;
						ph=m.asin(x/r)-sp/180*p%360;
						o.x=cX+r*m.sin(ph);
						o.y=cY+r*m.cos(ph);
						arc(o.x,o.y,o.s,r2,c2,c2);
					}
				}
			}

		bP(c1,c1);
		c.font="20px Arial";
		c.fillText("Click globe!",5,w);
	}

	setTimeout(rn,w);
}

nl=null;	// init game variables,constants and pointers
f=false;
t=true;
z=10;
w=20;
h=100;

// appends mouse and touch (mobile devices) event for player handling
a.onmousedown=function(){mC=t;d=D();};

// sets new snow object array on mouseclick and initializes movement of snow
a.onmouseup=function(){mC=f;sp=(D()-d)/h;for(i=0;i<mx;i++)do{ts=sw[i]={x:((cX+sz)-2*sz*rd()),y:((cY+sz)-2*sz*rd()),s:m.ceil(3*rd())}} while(sq(pw(cX-ts.x)+pw(cY-ts.y))>=sz-ts.s)};

c0="#000",c1="#993333";c2="#EEE"; 	// game colors
cG=["#CCCCBB","#9999CC"];
mC=f;	// is mouse down?
m=Math;
rd=m.random;
sq=m.sqrt;
p=m.PI;
r2=2*p;
wd=a.width;
ht=a.height;
cX=wd/2;
cY=ht/2;
sz=m.min(wd,ht)/2-w;	// radius ball
c3=c.createLinearGradient(5,h,50,w,z,2);	// background gradient
for(i=0;i<2;i++)c3.addColorStop(i,cG[i]);
mx=500; // max snow
//nlCnt=0;		// counter variable for null snow objects
sw=[];
rn();