// Change the Canvas to be the size of the window  and setting global Width and Height variables
var M=Math,W=window,G=c.width=W.innerWidth,J=c.height=W.innerHeight,

	// Reduce characters needed to call re-used Math functions
	O=M.cos,I=M.sin,P=parseInt,

	// Set up the 3D world's properties
	t=rX=rY=rZ=oX=oZ=0,N=[],mX=G/2,mY=J/2,mZ=160,U=.13,Q=20;

// Convert 3D point-data to 2D screen co-ordinates
function p(x,y,z,p){
  p=r(x+oX,y+oY,z+oZ);
  return{x:mX+p.x*mZ/(p.z+mZ),y:mY-p.y*mZ/(p.z+mZ),z:p.z};
};

// Return an point as an object transformed through a Rotation Matrix
function r(x,y,z){return{
  x:O(rY)*x+0*y+I(rY)*z,
  y:0*x+1*y+0*z+0,
  z:-I(rY)*x+0*y+O(rY)*z
}};


// Generate the Magic Carpet
W=H=18;_=W*H;Z=12;x=0;
for(var i=0;i<W*H;i++){
	N[i]={x:(i%W*Z)-(W*Z/2),y:0,z:(P(((i%(_))-x%H)/W)*Z)-(W*Z/2)};
}

// Define The Draw Loop
setInterval(function(){
	t++;
	a.fillStyle="rgba(0,0,0,.1)";
	a.fillRect(0,0,G,J);

  // Rotate the world and modulate the Y offset (Y is up and down, Z is near to far)
  rY+=.005;
  oY=I(t/50)*50;

	// Loop through the vertexes
	for(var i=0;i<_;i++){
		var x=i%W, y=P((i%(_)-x%H)/W);
		n=(y*H)+x;
		v=n+W+1;

		// If the values are within bounds
		if(N[v]&&x<H-1){

			// Calculate the 2D co-ordinates to draw between the 3D vertices on the screen
	    E=p(N[n].x,N[n].y+=I((t+x*20)/Q)*U,N[n].z);
			R=p(N[n+1].x,N[n+1].y+=I((t+x*24)/Q)*U,N[n+1].z);
			T=p(N[n+W].x,N[n+W].y+=O((t+y*3)/Q)*U*2,N[n+W].z);
			Y=p(N[v].x,N[v].y+=O((t+y*13)/Q)*U,N[v].z);

			// Draw paths to the Canvas
			a.beginPath();
				a.moveTo(E.x,E.y);
				a.lineTo(R.x,R.y);
				a.lineTo(Y.x,Y.y);
				a.lineTo(T.x,T.y);
			a.closePath();
			a.strokeStyle="rgb("+(P(.5+ Math.abs(I(y+t/10)*255)))+",128,255)";
			a.stroke();
		}
	}
},0);