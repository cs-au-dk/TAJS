// Author : Alaa-eddine KADDOURI  
// http://ezelia.com/
// a minimalistic pong with simple AI, scores  and a little twist

onresize(300,150);


var e;

b.onkeydown = function(ev) {

e=ev
};
b.onkeyup = function(ev) {

e={which:0}
};
f='fillStyle';


//reduced names :)
c.T=c.fillText;
c.R=c.fillRect;
R=Math.random;
S=Math.abs;
T=Math.sin;

//e = event
//s2 and s1 hold scores
//w and h are canvas width and height
//x and y are ball coords
//v = x velocity, z = y velocity
//A and B are players 1 and 2 paddles Y pos
e=s2=s1=0;w=300;x=h=150,y=10,v=1,z=2;A=B=20;

bullet1x = bullet1y = 0;
bullet2x = bullet2y = 0;

hold = false;

rflash = 0;
bflash = 0;
//Game loop

setInterval(function() {
	if (!hold) {
	//handle events, controlling player paddle
	//UP keycode is 38 and DOWN keycode is 40 so we use the middle value "39" to determin direction
	if(W=e.which) A += W==38 ? -2 : W==40 ? 2 :0;
	
	
	
	W==39 && !bullet1x ? bullet1x=1 : 0;
	if (bullet1x==1)
	{
		bullet1y = A+10 ;
	}
	
	
	//simplistic artificial intelligence : controlling CPU paddle 
	//here the CPU paddle follow the ball Y position when the following conditions are met :
	//  v > 0 : the ball direction is from player to CPU
	//  x > ~~(Math.random() * 200) + 100) : the ball position passed a threshold, otherwise CPU will allways win.
	(v>0 && x > ~~(R() * 200) + 80) && (y < B+10 ? B -= 2 : B += 2);
	//also try to avoid the opponent dragon
	(S(bullet1y-B)<=30 && R()>0.8 ) && (y-B < 0 ? B -= 2 : B += 2);
	
	
	R()>0.98 && S(A-B) < 20 && !bullet2x ? bullet2x=w-1 : 0;
	if (bullet2x==w-1)
	{
		bullet2y = B+10;
	}
	
	
	//check collision with paddles and invert X velocity
	x < 8 && y > A && y< A+20 || x > 290 && y > B && y< B+20 ? v = -v:0;		 
	
	//invert Y velocity if the ball collide with walls
	y > h || y < 0? z = -z :0;
	
	
	bullet1x > 294 && bullet1x < w && bullet1y > B && bullet1y < B+20 ? (x=w+1,rflash=6) : 0;
	
	bullet2x > 2 && bullet2x < 8 && bullet2y > A && bullet2y < A+20 ? (x=-1,rflash=6) : x;
	
	hold = (x < 0 || x > w);
	hold ? (setTimeout('hold=0', 2*w),bullet1x=bullet2x=0,bflash=rflash?0:4):0;
	//ball is behind paddles ==> update scores
	x<0 ? s1++ : (x > w) ? s2++ :0;
	
	
	
	//reset game => center the ball
	x > w || x < 0 ? x=h :0; 
		
	//limit player paddle movement, prevent going outside canvas
	A < 0? A = 0 : (A > 130? A = 130 : 0);
	
	//update ball coords
	x+=v;
	y+=z;	

	
	
	bullet1x > w+4 ?bullet1x=0:0;
	bullet1x ? (bullet1x+=2,bullet1y+=~~(T(x/5)*4)) :bullet1y=-20;
	
	
	
	
	bullet2x < -4 ?bullet2x=0:0;
	bullet2x ? (bullet2x-=2,bullet2y+=~~(T(x/5)*4)):bullet2y=-20;
	
	
	}
	//draw
	//clear canvas
	c[f]=rflash%2 ? '#f00' : ( bflash%2 ? '#00b' : '#fff');
	//c[f]=bflash%2 ? '#f00' : '#fff';
	c.R(0,0,w,h);
	
	
	c[f]='#00b';
	//draw ball
	c.R(x, y, 4,4);
	
	c[f]='#000';
	//draw paddle 1
	c.R(2, A, 6,20);
	
	//draw paddle 2
	c.R(294, B, 6,20);

	c[f]='#f00';
	c.R(bullet1x-3,bullet1y-2, 6,4);
	c.R(bullet1x+3,bullet1y-4, 2,2);
	c.R(bullet1x,bullet1y-4, 2,2);

	c.R(bullet2x-3,bullet2y-2, 6,4);
	c.R(bullet2x-6,bullet2y-4, 2,2);
	c.R(bullet2x,bullet2y-4, 2,2);
	
	
	//draw scores
	c.T(s2,20,10);
	c.T(s1,280,10);
	
	rflash = rflash > 0 ? rflash-1 : 0;
	bflash = bflash > 0 ? bflash-1 : 0;
}, 16);