// Delete the fullscreen <canvas> element to make a new, smaller one (300x150):
d();
var a = document.createElement("canvas"),
	c = a.getContext("2d"),
	
	// Shorthand values (save some precious characters):
	l = 100,
	m = 200,
	n = 300,
	o = 142,
	j = 120,
	t = [o,j,98,76],
	v = "Black",
	k = "#E6E6FA",
	e = 0,
	
	// Storage for X and Y drawing positions, respectively:
	x = [0],
	y = [0],
	z = [l,l,l,l],
	
	p = 0,// Fall Speed.
	q = 0,// Says if the player is on the ground (1) or not (0).
	r = 0,// Says if the jump can be charged (1) or not (0).
	s = 0,// Storage for the jump strength.
	u = 0,// Actual Score.
	g = 0,// HighScore.
	h = 0,// Says if the game is frozen (1) or not (0).
	
// Draws a rect:
d = function(q,w,r,t,k)
	{
	c.beginPath();
	c.rect(q,w,r,t);
	c.fillStyle = k;
	c.fill();
	c.strokeStyle = "rgba(0,0,0,0)";
	c.stroke();
	}

document.body.appendChild(a);

function f()
	{
	// If the gameplay is active, your Score will increase + your gravity will react:
	if(h==1)
		{
		u++;
		p>5?p=5:p+=.3;y[0]+=p;
		}
	else
		{
		x = [16,0,l,m,n];
		y = [134,o,o,j,j];
		}
	
	// Check fall speed in order to prevent a 'already-charged energy' glitch/bug/whatever.
	if(p>.3)
		{
		r = 0;
		s = 0;
		}
	
	// Check if jump is enabled, and if it is, it'll be charged:
	r==1?s>=5?s=5:s+=.3:s=0;
	
	e = y[0]+8;
	
	// Draws the sky:
	d(0,0,n,150,"#1E90FF");
	for(i=1;i<5;i++)
		{
	// Check for collision between X and Y coordinates, respectively (still crap, but works):
	if(x[0]+8>x[i]&&x[0]<x[i]+l+8){if(e<y[i]+4){if(e>=y[i]-3&&e<=y[i]+3)
		{
		y[0] = y[i]-8;
		p = 0;
		q = 1;
		}}
	// GameOver goes here:
	else
		{
		h = 0;
		if(u>g)g=u;
		}}
		
		// Draw some building's shadows to make layer 2 backgrounds:
		d(x[i],y[i]-30,l+8,m,k);
		}
	
	for(i=0;i<5;i++)
		{
		if(h==1)i>0?x[i]<-l?x[i]=l+m:x[i]-=3:x[i]=x[i];	 	 	  // Move all blocks, ignore char.
		x[i]==l+m?y[i]=t[Math.floor((Math.random()*4))]:y[i]=y[i];// Choose a random height.
		i==0?d(x[i],y[i],8,8,v):d(x[i],y[i],l+8,m,v);			  // Draw char/blocks.
		}
	
	// Show/hide jump-o-meter:
	if(r==1)
		{
		d(0,146,s*75,2,k);
		}
	
	// Draw Scoreboard:
	d(0,0,n,9,k);
	c.fillStyle = v;
	c.fillText(g+"/"+u,0,8);
	
	requestAnimationFrame(f);
	}

// Mousedown here will let you charge your jumping strength...
a.onmousedown = function()
	{
	if(h == 1 && q == 1 && p == 0)
		{
		q = 0;
		r = 1;
		s = 0;
		}
	// Game Start goes here (yeah, I MUST inform you about that):
	else
		{
		h = 1;
		u = 0;
		p = s = 0;
		}
	};

// ... while Mouseup will discharge all the brute force!
a.onmouseup = function()
	{
	if(h == 1 && r == 1 && p == 0)
		{
		p =-2.55-s;
		r = 0;
		}
	};

f();