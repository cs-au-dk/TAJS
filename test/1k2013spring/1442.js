// "BEAUTIFIED" SOURCE, made from the much dirtier original one (10kB limit here, so to take a look at the implementation process, or at the tricks used, check my website)

// Maps:
var facesSphere=[], facesCube=[],	// Meshes
	NB_DIV = 16,					// Number of rows and colums per cube's face, which means our meshes will be made of 6*16*16 vertices
	SIZE = 4,						// Dimension of our meshes
	RADIUS = SIZE/2,				// Mid-dim
	
	T = 0, 							// Time
	pulseTans = [],					// Array to contain the "pulse" requests made by the user when clicking.
	
	wHeight, wWidth, 				// Window dimensions.
	camDist = 4,					// Camera distance to the origin
	angleYaw = angleHeading = 4,	// Angles defining the camera orientation
	X = navigator.vendor?1:2; 		// The demo is so slow with FF & Op that I decided to reduce the number of vertices for these browsers.
	// navigator.vendor -> String which seems only defined / not empty for Chrome

///
// Given the subdivision indices, generates the corresponding vertex for the cube and sphere meshes (centered on the origin), and gives it to the current triangle faces.
// Parameters:
//	- i : Row num
//	- j : Col num
//	- face : Face of the cube we are currently dealing with
//	- trId : index of the current faces
// Returns: the distance of the cube vertex from its origin.
///
function GenerateVertex (i, j, face, trId) {
	// First we generate the cube vertex. Using some modulos on the face index, we can cover each case, by swapping the coordinates order or moving the vertex along the depth axis:
	var coord = [i/NB_DIV*X*SIZE-RADIUS, j/NB_DIV*X*SIZE-RADIUS, RADIUS*(1-2*(face%2))], 
		x = coord[face%3], y = coord[(face+1)%3], z = coord[(face+2)%3];
	facesCube[trId].push(x,y,z);
	
	// Then the sphere vertex, by "cube-mapping" (the following formula speaks for itself):
	var dist = Math.sqrt(x*x+y*y+z*z);
	facesSphere[trId].push(x/dist*RADIUS, y/dist*RADIUS, z/dist*RADIUS);	
	return dist;
}

var trId = 0;
// To save some bytes, we iterate desc. The idea is that for each element we iterate on, we evaluate the triangles faces of the square which as for top-left corner this element.
for (var f = 6; f--;)
	for (var i=NB_DIV/X; i--;)
	// X --- o	Schema representing the faces extracted from a square. X is the current element.
	// |  \  |
	// o --- o
		for (j = NB_DIV/X; j--;) for (k=2;k--;) // We iterate 2 times for each square in order to generate the 2 corresponding triangle-faces.
			// We store each face information into an array [V1.X, V1.Y, V1.Z, V2.X, V2.Y, V2.Z, V3.X, V3.Y, V3.Z, color]:
			facesSphere[trId] = [],
			facesCube[trId] = [],
			GenerateVertex(i,j, f, trId), 		// 1st vertex (top-left corner of the square)
			GenerateVertex(i+1,j+1, f, trId),	// 2nd vertex (bottom-right)
												// 3rd vertex - the one varying (either corresponding to the element on the next row, or the one on the next col). We use a modulo 2 on the face's id to select the good one.
			facesSphere[trId].push(GenerateVertex(i+k,j+1-k, f, trId)), // Will be useful to add some visual effects during the rendering.
			// Byte saving trick:(faceId+1)%2 = k. And we don't mind which triangle we generate first, so k makes a cheaper way to distinguish them.
			trId++;

///
// Special tweening...
// Parameters:
//	- cubeCoord - Coordinate from the 1st mesh
//	- sphereCoord - Coordinate from the 2nd mesh
// 	- t - Tween coef
//	- i - Just another parameter (the time) to make the tweening looks more random
//	- pulseCos - Another one, this time adding sometime some "holes" in the results
// Returns: the "intermediate" value
///
function Tween (cubeCoord,sphereCoord,t,T,pulseCos) {
	return Math.cos(T/47+t)*cubeCoord*(pulseCos+t)-Math.sin(T/31+t)*sphereCoord*(1-(pulseCos+t));
} 

///
// Cubic pulse - Thanks to Inigo Quilez (for this function and all the nice articles)
// Parameters:
//	- x - Input value
//	- c - Center of the pulse
//	- w - Width of the pulse
//	Returns: a cubic value if x in [c-w;c+w], 0 otherwise.
///
function Pulse (x,c,w) {
	var h=Math.abs(c-x)/w;
	return h>1?0:1-(3-2*h)*h*h;
} 

///
// Renders the scene.
///
function Paint () {
	// Clearing the canvas:
	a.fillRect(0, 0, wWidth=c.width=innerWidth-21,wHeight=c.height=innerHeight-21);
	wWidth/=2; wHeight/=2;
	var screenCoord = [], // Will contain the projection of the 3D vertices on the 2D screen.
	
	// Some values for the tweening:
	coefTween = Math.cos(++T/23),
	
	// We add 2 "pulses" to the scene:
	//	- A regular one (pulseCos), adding a small disturbance creating "holes" in the tweened mesh.
	//	- A one requested by the user (pulseTan), adding stronger disturbances the more the user clicks.
	pulseCos, pulseTan = 0;
	for(var z in pulseTans){ // We iterate on the array of pulse requests to evaluate the total value of this pulse:
		// The request is defined by a value representing the lifespan of the corresponding impulse. So we decrement it at each rendering.
		pulseTan+=Pulse(pulseTans[z]--,49,49); 
		pulseTans[z]||pulseTans.shift(); // Clean, but optional: we remove the request out of the array once its lifespan reaches 0.
	}
	pulseTan *= pulseTan;	
	pulseCos = Pulse(T%300,99,99)/2;
	
	var y=-Math.cos(angleYaw+T/31),w=-Math.sin(angleYaw+T/31),z=Math.cos(angleHeading+T/47),x=Math.sin(angleHeading+T/47); // To avoid repeating the callings.
	
	for (l in facesCube) {  // Not optimal, but shorter.
		screenCoord[l] = []
		var coef = camDist*(1-Math.cos(T/47+Math.tan(facesSphere[l][9])*pulseTan))*Math.cos(facesSphere[l][9]*pulseCos); // A coef taking into account the camera distance and the pulses effects.
		for (j = 9;j;) { // For each vertex of the face.
			// 3D projection. See https://en.wikipedia.org/wiki/3D_projection to find the whole linearized formula ans explanations.		
			// ... but actually in this demo, I decided to invert the depth, making the rendered mesh looks a bit unreal, insubstantial. To do so, I add the camera's position (coef) to the coordinates instead of substracting it. (Though, by pressing any key, it is possible to re-invert the depth to get it right)
			var v=Tween(facesCube[l][--j], facesSphere[l][j], coefTween, T, pulseCos)+coef*z,
			u=Tween(facesCube[l][--j], facesSphere[l][j], coefTween, T, pulseCos)+coef*x*y,
			r=Tween(facesCube[l][--j], facesSphere[l][j], coefTween, T, pulseCos)+coef*x*w;
			
			// For each face, we store its screen information into an array [P1.Z, P1.Y, P1.X, P2.Z, P2.Y, P2.X, P3.Z, P3.Y, P3.X, origDist], with Z the depth from the camera:
			var k = y*u+w*r, N = z*v+x*k;
			screenCoord[l].push(N,(x*v-z*k)/N*wHeight + wHeight +.5|0,(w*u-r*y)/N*wWidth + wWidth +.5|0);
		}
		screenCoord[l][9] = facesSphere[l][9]
	}
	
	// To draw the mesh, we use the simple Painter's algorithm (which works well in our case, since we don't have overlapping elements): we sort the faces by decreasing depth (far -> near - we will simply use P3.dist instead of the average) and draw them it this order.
	screenCoord.sort(function(H,S){return H[6]-S[6]}); // Sorting
			
	for (l in screenCoord) // Evaluating the face color and drawing it:
		a.fillStyle = a.strokeStyle='hsl('+[
			399*coefTween/screenCoord[l][9],// Hue - Function of the time and of the original distance to the origin.
			'50%',							// Saturation - constant
			l/75*X*X*Math.cos(pulseCos/7)	// Luminosity - Function of the periodic pulse + Cheap fog effect by using the sorted index
		]+'%)',
		a.beginPath(),
		a.moveTo(screenCoord[l][8], screenCoord[l][7]),
		a.lineTo(screenCoord[l][5], screenCoord[l][4]),
		a.lineTo(screenCoord[l][2], screenCoord[l][1]),
		a.closePath(),
		a.stroke(),
		a.fill()
};

b.onclick = function(){	// New impulse on click
	pulseTans.push(99);
};
b.onmousemove = function(H) {	// We tie the mouse position with the camera orientation.
	angleYaw = 2*H.clientX/wWidth;
	angleHeading = 2*H.clientY/wHeight;
}
b.onkeyup=function(A,B){	// We give the possibility to switch between the "inverted depth mode" and the normal one, by pressing any key.
	camDist = -camDist;
}

setInterval(Paint,16);

/*
How to minify the demo? Tricks I learned...
	- Closure Compiler and JsCrush are your friends
	- Check github.com/jed/140bytes/wiki/Byte-saving-techniques to learn more dirty tricks.
	- Omit "var", noone will mind
	- Inline when possible (don't use variables for 1-char-long const values, limit the number of functions, ...)
	- Limit the size of your const (use string instead of array, 9 instead of 10, ...)
	- Save delimiters by doing stuff within unused function or loop arguments
	- If/else can be replaced by ternary assignations or bitewise operations
	- Use the fact that the operator "=" assigns a value but also returns it
	- Learn operators precedence to get rid of parentheses.
	- To make the best out of JsCrush:
		- Create patterns in your code:
			- Limit the number of variables, and reuse them smartly (ex: if you keep using the variable A for arrays and i for the indices, every expression "A[i]" will be crushed, saving you 3n-7 bytes, with n the number of times this expression appears)
			- Rearrange your expressions to create new pattern, by ordering the symbols appearances (ex: rearrange "m=i*r-9;q=r*j-9;" into "m=i*r-9;q=j*r-9;" to make the pattern "*r-9;" appears)
			- Use exactly same function signatures (take the longest one and assign it to every function)
		- Check the output of Closure Compile beforehand. It may have altered your signatures or symbols arrangements. It also adds an "useless" semicolon at the end of your script.
		- Save 2 bytes by changing the jsCrush bootstrap: replace "for(Y=0;$='CHAR'[Y++];)with(_.split($))_=join(pop());" by "for(Y in $='CHAR')with(_.split($[Y]))_=join(pop());"
	- Adapt your equations to the context to avoid useless operations or steps / Look at the whole picture

Thanks for the attention!
*/