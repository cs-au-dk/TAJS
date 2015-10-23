// Lit waves. Heavily tweaked for better compression with http://iteral.com/jscrush.
// Simulation is cheap, rendering is expensive.

// Fill all available space
c.innerHTML='<style>*{margin:0}</style>';
X=Y=M=Math.min(w=c.width=innerWidth,h=c.height=innerHeight);

onmousemove=function(b,c,d,e){
  X=b.clientX-w/2;
  Y=b.clientY-h/2
};

// Wrapped read. Note that coordinates are swapped! (Saves a few bytes in compression.)
W=function(b,c,d,e){
  return b[(d%32)*32+c%32]
};

// Reciprocal multiply-add with optional 2D Laplacian.
L=function(b,c,d,e){
  b=b.slice(0);
  for(i=32;i--;)
    for(k=32;k--;)
      b[i*32+k]+=1/d*(e?-4*c[i*32+k]+W(c,k,i+1)+W(c,k,i+31)+W(c,k+31,i)+W(c,k+1,i):c[i*32+k]);
  return b
};

// 2D rotation. e selects which component of result you're interested in.
S=function(b,c,d,e){
  return Math.cos(b)*(e?d:c)+Math.sin(b)*(e?-c:d)
};

// heights
s=[];
// accelerations
l=[];
// velocities
v=[];
// zero
n=[];

// initialize starting arrays
for(i=32;i--;)
  for(k=32;k--;)
    f=(i/16-1)*3/2,
    y=(k/16-1)*3/2,
    // heart formula from http://js1k.com/2012-love/demo/1228
    s[i*32+k]=Math.pow(f*f+y*y-1,3)<f*f*f*y*y?.6:0,
    n[i*32+k]=v[i*32+k]=l[i*32+k]=t=0;

// smooth heights
s=L(s,s,8,1);
s=L(s,s,8,1);
s=L(s,s,8,1);
s=L(s,s,8,1);
s=L(s,s,8,1);

setInterval(function(b,c,d,e){
  // sim step: velocity verlet integration, dt=1/8
  // update heights
  s=L(s,L(v,l,16),8);
  // new accelerations, constant is for frequency-dependent damping
  b=L(L(n,s,1,1),L(n,v,320,1),1);
  // update velocities, constant is for uniform damping
  v=L(v,L(L(l,l=b,1),v,-5e3),16);

  // clear screen
  a.fillStyle='rgb(0,0,0)';
  a.fillRect(0,0,w,h);
  // screen coords, each element is [x,y]
  H=[];
  // interpolated zs
  c=[];
  // quads to draw, each element is [quad index, depth sort key]
  b=[];
  // integer offset
  g=-~t;
  // fractional offset (for interpolation)
  d=g-t;
  // Calculate vertex screen coordinates.
  for(i=32;i--;)
    for(k=32;k--;)
      // find world-space x and y
      f=S(t/32,i/16-1,k/16-1),
      y=S(t/32,i/16-1,k/16-1,1),
      // calculate interpolated z
      e=c[i*32+k]=d*W(s,k,i+g)+(1-d)*W(s,k,i+g+1),
      // perspective transform
      r=M/Math.pow(f*f+y*y+4*f+e*e-4*e+8,1/2),
      H[i*32+k]=[w/2+r*y,h/2-r*(f+e)/2],

      // to avoid wrapping issues later, only draw quads away from the "low" edges
      i*k&&b.push([i*32+k,f]);

  // depth sort
  b.sort(function(b,c,d,e){return b[1]-c[1]});

  // Light and draw scene. Pretend all the quads are flat.
  for(t+=1/6;i=b.pop()[0];)
    // normal components
    a.beginPath(f=c[i]-c[i-32],
                y=c[i]-c[i-1]),

    // diffuse lighting based on mouse position: angle is angle, radius is elevation (sort of)
    e=S(Math.atan2(16*Math.pow(X*X+Y*Y,1/2),M),1/16,S(Math.atan2(X,Y)-t/32,y,f,1))/Math.pow(f*f+y*y+1/256,1/2),
    // other lighting: ambient, fake shadows, etc.
    e=-~(320*(e>0?e:0)+116*(e<0?e:0)+116+116*c[i]),

    // fake some (dull) specularity by sending bright patches to white
    // use RGB because HSL is slow
    a.strokeStyle=a.fillStyle='rgb('+[e,e-256,e-216]+')',
    // draw quad
    a.moveTo.apply(a,H[i]),
    a.lineTo.apply(a,H[i-1]),
    a.lineTo.apply(a,H[i-33]),
    a.lineTo.apply(a,H[i-32]),
    // have to use stroke() to avoid ugly lines between quads
    a.stroke(),
    a.fill()
},32)