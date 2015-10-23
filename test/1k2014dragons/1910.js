// Shorten GL context function names. Avoided bind()ing since it didn't produce significant compression.
for(i in g) g[i.match(/^..|[A-Z]|\d\w$/g).join("")]=g[i]
// Alias sin, cos and pi/8 to single char variables since those come up in a couple of places
_=Math.sin;$=Math.cos;O=Math.PI/8;

// x = 2 * sin(z), y = 2 * cos(z)
function R(z) {
  return [2*_(O*z),2*$(O*z)]
}

// Compile the shader of source s and type t
function cS(s, t) {
  h = g.crS(t)
  g.shS(h, s)
  g.coS(h)
  return h;
}

var p = g.crP();
// Projection matrix * View matrix * point (model matrix is always identity); send the light intensity z to the fragment shader
g.atS(p,cS("uniform mat4 m,v;attribute vec3 p;varying lowp float z;void main(){vec4 P=gl_Position=m*v*vec4(p,1.);z=1.-clamp(P.z-.5,.0,1.);}",35633))
// Color is rgb(0.0, 0.5, 1.0) * z
g.atS(p,cS("varying lowp float z;void main(){gl_FragColor=vec4(vec3(.0,z*.5,z),.8);}",35632))
g.liP(p)
g.usP(p)

// Ugly hard coded and heavily rounded perspective matrix, good for 16:9
g.unM(g.geUL(p,'m'),0,[-.1, 0, 0, 0, 0, -.2, 0, 0, 0, 0, -1, -1, 0, 0, -.1, 0]);
g.clC(.2,.2,.2,1);
g.en(2929);

// Build the tube geometry
s = [], i = [], W = 32, J=6.28/W;
for(I = 0; I <= W; I++) { // 32 skewed cylinders
  w = R(I); // get point
  for(j = 0; j < W; j++) { // each circle is built with 32 lines
    s.push($(J * j) + w[0], _(J * j) + w[1], - I/4) // a vertex on the circle
    K=W*I;
    i.push(
      K + j,
      K + (j + 1) % W, // current vertex to next vertex on the circle
      K + j,
      K + W + j // current vertex to vertex with the same position on the next circle
    )
  } 
}

// build vertex and index buffers
m = g.crB()
g.biB(34962, m)
g.buD(34962, new Float32Array(s), 35044)
g.biB(34963, g.crB())
g.buD(34963, new Uint16Array(i), 35044);

(X = function(D) {

  // Calculate the point on the curve: divide time by 128 and reduce modulo 16 to loop
  d=(D/128)%16;
  requestAnimationFrame(X)
  g.clear(16640);
  E = R(d);
  // Calculate view matrix
  v = [
   1, 0, 0, 0,
   0, 1, 0, 0,
   0, 0, 1, 0,
   -E[0], -E[1], d/4, 1
  ];
  // vertexAttribPointer(getAttribLocation(), 3, FLOAT, uniformMatrix4fv(getUniformLocation(), 0, v), 0, 0)
  g.veAP(P=g.geAL(p,'p'), 3, 5126, g.unM(g.geUL(p,'v'),0,v), 0, 0);
  g.enVAA(P);
  g.drE(1, 4096, 5123, 0);

})()