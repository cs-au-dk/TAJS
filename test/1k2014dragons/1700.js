a.ontouchstart=a.onmousedown=function(e){	// appends mouse and touch (mobile devices) event for player handling
	if(e.clientX-a.offsetLeft-pl.x<0)pl.x-=15;
	else pl.x+=15;
}

bP=function(sk,fl,lw){	// begins a canvas path - helper function
	c.beginPath();
	c.lineWidth=lw;
	c.strokeStyle=sk;
	c.fillStyle=fl;
}

eP=function(){	// ands, strokes and fills a canvas path - helper function
	cP();
	c.stroke();
	c.fill();
}

arc=function(x,y,sz,ln,sk,fl,lw){	// draws a canvas arc -helper function
	bP(sk,fl,lw);
	c.arc(x,y,sz,0,ln);
	eP();
}

bg=function(){	// begins a new game
	pl={w:18,h:18,c:"#4169E1"};
	init();
	pts=0;
	spd=3;
	lp()
}

nwSnw=function(){	// returns a new snow object
	var o={'x':flr(wid*rd()),'y':flr(hgt*rd()),'s':Math.ceil(3*rd())};
	return o;
}

init=function(){	// resets game values
	var aC,i;
	for (i=0;i<mxS;i++) snw[i]=nwSnw();
	end={w:flr(wid/3),h:3,x:flr((wid-wid/3)*rd()),y:hgt-25};
	for(i=0;i<mxT;i++){
		aC=['#BDB76B','#556B2F'];
		iC=flr(aC.length*rd());
		aTr[i]={w:24,h:32,x:flr((wid-24)*rd()),y:flr((hgt-232)*rd())+100,c:aC[iC]};
	};
	pl.x=flr((wid-pl.w-50)*rd())+50;pl.y=5;
}

cP=function(){c.closePath()}	// helper function to close a path

fT=function(t,x,y){c.fillText(t,x,y)}	// helper function to draw a custom text

shp=function(cd,sk,fl,lw){	// draw a custom canvas shape by points - helper function
	bP(sk,fl,lw);
	var o;
	for(var i=0;i<cd.length;i++){
		o=cd[i];
		if(o[0]!=nl)c.moveTo(o[0][0],o[0][1]);
		if(o.length>3)c.quadraticCurveTo(o[3],o[4],o[1],o[2])
		else c.lineTo(o[1],o[2]);
	}
	eP();
}

lp=function(){	// main game loop function for drawing and all handling (saves space) 
	bP(grd,grd,1);
	c.fillRect(0,0,wid,hgt);	// refresh background
	cP();
	var i,p,w,x,h,y,xf,yf,x1,x2,x3,y1,y2,cl;
	for(i=0;i<aTr.length;i++){	// draw trees
		p=aTr[i];
		w=p.w;
		x=p.x;
		h=p.h;
		y=p.y;
		xf=x+w/2;
		yf=y+h-15;
		shp([[[xf,y],x,yf],[nl,x+w,yf],[[x,yf+1],x+w-2,yf,xf,y+h-7]],p.c,p.c,2);
		shp([[[xf-1,yf+5],xf-1,yf+13]],wCl,wCl,5);
	}
	p=pl;cl=lgr;w=p.w;x=p.x;y=p.y;h=p.h;x1=x+w/2;x2=x1-5;x3=x1+5;y1=y+h;	// draw player
	var cs=[[[[x2,y+8],x2,y+21]],[[[x3,y+6],x3,y+19]],[[[x1-1,y+p.h-3],x1-4,y1+12]],[[[x3,y1-3],x1+2,y1+12]]];
	for (i=0;i<cs.length;i++)shp(cs[i],cl,cl,2);
	arc(x1,y+3,3,2*pi,p.c,p.c,2);
	shp([[[x1,y+3],x1,y1-5],[[x1,y+10],x2,y+11],[[x1,y+10],x3,y+8],[[x1,y1-5],x1-3,y1+5],[[x1,y1-5],x1+3,y1+5]],p.c,p.c,2);
	arc(x1,y+4,3,pi,csk,csk,2);
	ac=[[x1-2,y+5],[x1+2,y+5]];
	for (i=0;i<ac.length;i++) arc(ac[i][0],ac[i][1],1,pi,gry,gry,1);
	p=end;w=p.w;x=p.x;y=p.y;x1=x+10;x2=x+w;x3=x+w/2;y1=y-120;y2=y-80;	// draw finish line
	shp([[[x,y],x1,y1],[[x2,y],x1+w,y1]],wCl,wCl,3);
	shp([[[x1,y1],x+6,y2,x+15,y1+20],[nl,x2+8,y2,x3,y-70],[nl,x2+12,y1,x2+15,y1+20],[nl,x1,y1,x3,y1+10]],blk,wt,1);
	bP(gry,gry,1);
	c.font="italic bold 24"+fnt;
	fT("FINISH",x3-30,y-90);
	for (i=0;i<snw.length;i++){	// draw snow
		snw[i].y+=3;
		if (snw[i].y>=hgt) snw[i]=nwSnw();
		arc(snw[i].x,snw[i].y,snw[i].s,2*pi,csn,csn,1);
	}
	bP(cr,cr,1);	// draw points
	c.font="bold 20"+fnt;
	fT("Points: "+pts,wid-180,30);
	cP();
	pl.y+=spd;
	var o,pX=pl.x,pY=pl.y,pW=pl.x+pl.w,pH=pl.y+pl.h;
	for(i=0;i<aTr.length;i++){	// player collides with a tree
		o=aTr[i];
		if(((pW>=o.x&&pH>=o.y)||(pX>=o.x+o.w&&pY>=o.y+o.h))&&(pW<=o.x+o.w&&pH<=o.y+o.h)){bg();return}
	}
	if(pX>=end.x&&pH>=end.y&&pW<=end.x+end.w){	// player has reached the finish line
		init();
		spd++;
		pts+=50;
		lp();
	}
	else if(pH>hgt) bg()	// player drove away
	else setTimeout(lp,20);
}
nl=null;	// init game variables,constants and pointers
fnt="px Georgia";
wCl="#8B4513";wt="#fff";cbg="#CCCCBB";gry="#333";lgr="#999";blk="#000";csn="#eee";cr="red";csk="#FFD39B";	// game colors
m=Math;
rd=m.random;
flr=m.floor;
pi=m.PI;
wid=a.width;
hgt=a.height;
grd=c.createLinearGradient(0,150,10,10,10,90);	// background gradient
grd.addColorStop(0,wt);
grd.addColorStop(1,cbg);
mxS=flr(wid/3);		// max snow
mxT=flr(wid/50);	// max trees
snw=[];
aTr=[];
bg();