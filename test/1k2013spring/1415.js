r=Math.random;

c.width=W=500;
c.height=H=535;

a.font="16px Arial";

w=0; // wrong move indicator
st=0; // start indicator

bl = "000000000000000"; //blank area (defines tile amount)
t=15; // tiles
s=32; // tile size

function clamp(v){return v<0?t+v:v%t}
function clone(o){return {x:o.x,y:o.y}}

// generate level
function gl() {
	P={x:r()*12|0+2,y:r()*12|0+2}
	p=clone(P);

	lvl="";for(i=t;i--;){lvl+=bl}

	x=0;lx=0;
	y=0;ly=0;
	wk=0;tc=0;

	for(l=16;l--;){
		p.x=clamp(p.x+x);
		p.y=clamp(p.y+y);

		i=p.y*t+p.x;
		lvl=lvl.substr(0, i)+"1"+lvl.substr(i+1);

		while((x==lx&&y==ly&&!wk)||tc=="1") {
			x=r()*4+-2|0; // -1,0,1
			y=0;while(!x+y){y=r()*4+-2|0}; // -1,0,1
		
			i=(p.y+y)%t*t+p.x+x%t;
			tc=lvl[i];
		}

		lx=x;ly=y;
		wk=wk||r()*6|0+2;
		wk--;
	}

	i=p.y*t+p.x;
	lvl=lvl.substr(0, i)+"E"+lvl.substr(i+1);
	E=clone(p);
}

function draw() {
	// bg & tiles
	a.fillStyle="#000";a.fillRect(0,0,W,H);
	a.fillStyle="#222";for(y=t;y--;){for(x=t;x--;){a.fillRect(x*s+x+3,y*s+y+38,s,s)}}

	// path
	a.fillStyle="#111";for(i=lvl.length;i--;){
		tc=lvl[i];
		if ((tc=="1"&&!st)||tc=="x") {
			x=i%t|0;y=i/t|0;
			a.fillRect(x*s+x+3,y*s+y+38,s,s);
		}
	}

	// player & goal
	a.fillStyle=w?"#f00":"#0f0";a.fillRect(P.x*s+P.x+3,P.y*s+P.y+38,s,s);
	a.fillStyle="#FFF";a.fillRect(E.x*s+E.x+3,E.y*s+E.y+38,s,s);

	// text
	a.fillText("green = you; white = goal; arrow keys = move; enter = start",10,25);
}

b.onkeydown=function(e) {
	e=e.keyCode;
	if(e==13)st=1;
	if(!st||w)return

	// velocity
	x=e==37?-1:e==39?1:0;
	y=e==38?-1:e==40?1:0;

	i=(clamp(P.y+y))*t+clamp(P.x+x);
	tc=lvl[i];

	LP=clone(P);
	P.x=clamp(P.x+x);
	P.y=clamp(P.y+y);

	// right path
	if(tc=="1"||tc=="S"){lvl=lvl.substr(0, i)+"x"+lvl.substr(i+1)}

	// end
	else if(tc=="E"){st=gl();}

	// wrong path, reset position
	else{
		w=1;window.setTimeout(function(){
			P=clone(LP);
			w=0;draw()
		},250);
	}draw();
}

gl();
draw();