//Global variables
p=new Array();
mx=0;			// mouse x
my=0;			// mouse y	
radious=1;		// radious
coef=0;
w=0;			// canvas width
h=0;			// canvas height.
ra=5;			// particle size.

//Seed class Pa because is a particle.
function Pa(ox,oy,c,s)
{
	this.x=0;		// center x coordinate
	this.y=0;		// center y coordinate
	this.ox=ox;		// offset x
	this.oy=oy;		// offset y
	this.c=c;		// colour
	this.speed=s;	// speed
	this.sx=0;		// shake x
	this.sy=0;		// shake y
}  

//Program start
window.onload=function() 
{
	a=document.getElementById('canv');
	c=a.getContext('2d');
	
	w=a.width;
	h=a.height;
	mx=w/2;
	my=h/2;

	a.addEventListener('mousemove', function(evt)
	{
		var r=a.getBoundingClientRect();
		mx=evt.clientX-r.left;
		my=evt.clientY-r.top;

		// Make 0,0 be the center of the canvas.
		dx=w/2-mx;
		dy=h/2-my;
		
		coef = Math.sqrt(dx*dx + dy*dy) / ((w+h)/14);

		// If mouse_y is near top or bottom change particle's size. 
		if(my < h*0.3 && ra > 1)
			ra--;
		
		if(my > h*0.7 && ra <10)
			ra++; 
	},false);
		
	// My custom loop funcion that recycles "for"
	myLoop(0); 
	
	// Animate
	animate();
};

// Clear canvas, update positions and render.
function animate() 
{
	c.clearRect(0,0,w,h);
	
	myLoop(1);
	
	// Canvas rectangle
	c.beginPath();c.lineWidth = 1;c.rect(0, 0, w,h);c.stroke();
	
	window.setTimeout(animate, 16);
}

//Perform initialization or update positions. Here the trick is recicle the function.
//	functionality=0 then function update positions.
//	functionality=1 then function does initialization.
function myLoop(functionality)
{
	for (var i=384;i>=0;i--)
	{
		if(functionality)
		{
			// Reduce code because there are many places that uses p[i].
			l=p[i];	
			
			// Update position using custom Xenon theorem (half path beetwen two point)
			// in this case a proportional distance beetwen source and target given by .t
			l.x+=(mx-l.x)/l.speed;
			l.y+=(my-l.y)/l.speed;
			
			l.sx=Math.random();
			l.sy=Math.random();
			c.beginPath();
			
			// Use "arc" to draw circle, but 6.28318-coefspecial effect.
			c.arc(l.x+l.ox+l.sx, l.y+l.oy+l.sy, ra,0,6.28318-coef);
			c.fillStyle=l.c;
			c.fill();
			c.strokeStyle=l.c;
			c.stroke();
		}
		else
		{
			// Convert to radian.
			angle = (137.5077 * Math.PI / 180 * i);	//You can replace "137,5077*PI/180" by "2.399" and win some bytes.
			
			// Each 5 loops, perform radious variation.
			if ((i % 5) == 0)						//You can replace "(i % 5) == 0" by "!(i%5)" and save some bytes.
				radious +=2;
			
			// Trick, use speed to generate color and make it depend on radious and win some bytes.
			speed = radious + 6;
			
			// Create and initialize particle item. 
			pr=new Pa(
				Math.cos(angle)*radious,
				Math.sin(angle)*radious,
				'rgba('+speed*2+','+speed+','+speed*12+','+(0.5)+')',
				speed);
			p.push(pr);
		}
	}
}