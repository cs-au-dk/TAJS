c.width = w = window.innerWidth;
c.height = h = window.innerHeight;
c.style.background = '#000';

var ran = Math.random;

var heart,
hearts = [],
hRad=20;
var r = 40;
for(var i =0; i<70; i++){
	hearts.push(
		{x:i*50,
		y:h*Math.random(),
		vel:{
			x:Math.random(),
			y:Math.random()},
			colour:'#'+Math.floor((16777215/2) + Math.random()*(16777215/2)).toString(16)
		}
	);
}
var cir,circles = [];
var i,j;
for(i =0; i<9; i++){
	circles.push({
		x:((i+1)*(w/10)), 
		y:((i%2)+1) * (h/3),b:1
	});
}

var linGrad = a.createLinearGradient(0,0,0,h);  
linGrad.addColorStop(0, '#00ABEB');  
linGrad.addColorStop(1, '#fff');  

setInterval( function() {
  a.globalAlpha = .8;
  a.fillStyle = linGrad;
  a.fillRect(0,0,w,h);
  a.font = hRad*1.6+'px Arial';
  for(i=0; i<hearts.length; i++ ) {
	heart = hearts[i];
	
	//check which cirle the hearts will intersect with and repels it
	for(j=0;j<circles.length;j++){
		cir = circles[j];
		var xDist = cir.x - heart.x;
		var yDist = cir.y - heart.y;
		var dist = (xDist*xDist) + (yDist*yDist);
		
		if(dist < (hRad+r)*(hRad+r)){	//intersection, repel the sphere
			cir.b=1.05;	//used to make the circle throb when it's hit
			heart.vel.x += -xDist;
			heart.vel.y += -yDist;
		}
	}	
	
	//work out the velocity of the heart, apply gravity and move it.
	heart.vel.x = Math.max(Math.min(heart.vel.x + ran()*0.4 -0.2,4),-4);
	heart.vel.y = Math.max(Math.min(heart.vel.y + 0.2,8),-8);
	heart.x += heart.vel.x;
	heart.y += heart.vel.y;
	
	//If the heart goes outside the boundaries, loop it to the other side
	heart.y = heart.y%(h+hRad*2);
	heart.x = (w + heart.x) % w;
	
	//the cirle
    a.fillStyle = heart.colour;	
	a.beginPath();
	a.arc(heart.x, heart.y, hRad, 0, Math.PI*2, true); 
	a.closePath();
	a.fill();
	
	//the heart
	a.fillStyle = '#fff';
	a.fillText('♥', heart.x-hRad/2, heart.y+hRad/2);
  }

  //draw the static circles
  for(i=0;i<circles.length;i++){
	a.fillStyle = '#fff';
	cir = circles[i];
	a.beginPath();
	a.arc(cir.x, cir.y, r*cir.b, 0, Math.PI*2, true); 
	a.closePath();
	a.fill();	
	cir.b=1;
  }
}, 30 );