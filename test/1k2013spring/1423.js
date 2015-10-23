// Flat-shaded metaballs using marching tetrahedrons in 1019 bytes of JavaScript
// Inopia/Aardbei 2013

// Grid dimensions
d=22;
g=d*d;

// Screen size. 
n=250;
o=n+n;

// Shortcut to Math
m=Math;

// Generate Polygonize function P. This function takes a set of four corner points and evaluates
// all eight possible combinations of those points being either inside/outside the iso surface,
// and generates polygons accordingly. There are 10 different polygons that can be created, and 
// their conditions are bit-packed into the numerical array below.
f="function P(v0,v1,v2,v3){u=0;";
for(t=i=0;i<4;i++)
	f+="if(v[I+v"+i+"].r<0)u|="+(1<<i)+";";
for(q in v=s=[51278,40221,55500,35292,58923,52810,59722,60745,59465,31544])
{
	l=s[q];
	f+="if(u=="+(l&15)+"||u=="+(~l&15)+")T.push([";
	for(i=0;i<3;i++)
	{
		l>>=4;
		f+=("{G:(C=v[v"+(l&3)+"+I]).G+(Q=-C.r/((Y=v[v"+(l>>2&3)+"+I]).r-C.r))*(Y.G-C.G),H:C.H+Q*(Y.H-C.H),z:C.z+Q*(Y.z-C.z),n:C.n+Q*(Y.n-C.n)},")
	}
	f+="]);"
}
f+="}";
eval(f);

// Main loop
setInterval(function()
{
	// Time
	t++;

	// Initialise sine functions. These dictate the movement of the balls, and the
	// morphing colors
	s=[];
	for(i=0;i<15;i++)
		s[i]=0.27*m.sin((t/10+33*i%13)/(0.5+0.1*(91*i%12)));
		
	// Compute vertices	
	for(l=0;l<d*g;l++)
	{
	
		// x,y 
		x=l/g/d-0.5;
		y=0.5-l%d/d;
		
		// z, screen-x, screen-y, and the normal need to be interpolated during 
		// polygonization, so keep them around in the vertex array. x,y are only
		// used for computing the iso surface.
		v[l]={
			z:z=0.5-l/d%d/d,
			
			// Project onto screen space
			G:x/(z+1)*o+n,
			H:y/(z+1)*o+n,
			
			// Initialize the iso, normal components to 0
			n:0,r:0
		};
		
		// 5 balls
		for(i=0;i<15;i+=3)
		{
			X=x+s[i];
			Y=y+s[i+1];
			Z=z+s[i+2];
			
			// Iso value
			v[l].r+=1/(p=X*X+Y*Y+Z*Z)-16;
			
			// Z-component of the vertex normal, for shading/culling
			v[l].n+=1/p/p*Z
		}
	}
		
	// Polygonize the iso surface. Each call to P polygonizes one of the six 
	// tethrahedrons in the grid cell.T is the list of generated polygons,
	// I is a global counter to where we are in the grid. The loop should
	// really be three nested ones from [0..d-2] per dimension, but I cheat
	// a little here by ignoring wrap-around problems - my blobs simply never
	// hit the edges :)
	T=[];
	for(I=0;I<21*21*21;I++)
	{
		P(0,d+1,d,g+d);
		P(0,d+1,g+d+1,g+d);
		P(0,g,g+d+1,g+d);
		P(0,g+d+1,1,d+1);
		P(0,g+d+1,1,g);
		P(g+1,g+d+1,1,g);
	}
	
	// Sort polygons back-to-front (painter's algorithm)
	T.sort(function(b,e){return b[0].z>e[0].z?-1:1});
	
	// Setting the canvas width/height also clears the canvas
	c.width=c.height=o;
	
	// Render polygons
	for(i in T)
	
		// Backface culling. No space to compute the face normal by averaging over the
		// edge normals, so just grab one corner point and cull conservatively. 
		if((S=T[i])[0].n<99)
		{
			// Compute face color based on the vertex normals. We have three
			// vertices and three color components (r,g,b), and each component
			// is computed from a corresponding vertex. I also mix in some sine
			// functions here from the 's' array to create a nice color morphing
			// effect.
			G="#";
			for(k in S)
				G+=m.min(m.round(64-S[k].n/(3-2*s[k])),255).toString(16);
			
			// Set stroke/fill color. 
			a.strokeStyle=a.fillStyle=G;
			
			// Draw polygon. We both fill and stroke the polygon because otherwise
			// we get ugly gaps between the polys.
			a.beginPath();
			a.moveTo(S[2].G,S[2].H);
			for(j in S)
				a.lineTo(S[j].G, S[j].H);
			
			a.fill();a.stroke();
		}
},50);