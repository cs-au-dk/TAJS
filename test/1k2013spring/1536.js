// E = eye horizon base offset
// x,y,z = pong ball center
// X,Y,Z = pong ball velocity vector component
E = x = y = z = X = Y = Z = -2;

B = b.style;
B.margin = 0;
B.overflow = 'hidden';
B.background = '#000';
B.cursor = 'none';

w = (c.width = b.clientWidth)/2;
h = (c.height = window.innerHeight)/2;

W = w*.8;
H = h*.8;
M = N = 0;
A = Math.abs

// test limit [-l,l]
function T(t,l,i) {
  return t<-l ?
           (i? A(i) : -l)
           :
           t>l ?
             (i? -A(i) : l)
             :
             (i? i : t)
}

c.onmousemove = function(e) {
  M = T(e.clientX - w, W);
  N = T(e.clientY - h, H);
}

/*
red trans:  r = 'rgba(255,0,0,0.5)'
red opac:   R = 'rgb(255,0,0)'
blue trans: l = 'rgba(0,100,255,0.5)'
blue opac:  L = 'rgb(0,100,255)'
*/
r = (R = 'rgba(255,0,0,')+'0.5)'
R += '1)'
l = (L = 'rgba(0,100,255,')+'0.5)'
L += '1)'
a.globalCompositeOperation = 'lighter';

a.f = a.fill;
a.b = a.beginPath;

// Plot Dot
function p(x,y,z,r) {
  s=z+2;
  a.b();
  a.arc((x+E*z)/s, y/s, r/s, 0, 2 * Math.PI);
  a.fillStyle = E<0 ? R : L;
  a.f();
}

// translate context to add 10px padding
a.translate(w, h);

setInterval(function() {
  a.clearRect(-w, -h, 2*w, 2*h);
  // Draw for each eye
  for ( E=-7; E<8; E+=14 ) {
    // Draw the playground dots
    for ( K=-1; K<=1; K+=.2 )
      // Lines of dots
      for (Q=-W;Q<=W;Q+=W/5) {
        p(Q, -H, K, 4);
        p(Q, H, K, 4);
      }
    
    // Draw the ball
    p(x, y, z, 20);
    // Draw pong racket
    a.fillStyle = E<0 ? r : l;
    a.strokeStyle = E<0 ? R : L;
    // racket pieces
    for (Q=0; Q<11; Q+=10) {
      a.b();
      a.rect(M-20-Q-E, N-15-Q, 40+Q*2, 30+Q*2);
      a.stroke();
    }
    a.f();
  }
  // Move the ball
  x+=X; y+=Y+=0.1; z+=Z;
  // kick ball
  X = T(x,W,X);
  Y = T(y,H,Y);
  //if ( y>0 && Y<0 && Y>-2 ) Y-=0.5
  if ( y>0 && Y<0 ) Y-=0.05
  if ( z>1 ) Z=-0.018;
  if ( z<-0.9 && z>-1.01 && M-40<x && M+40>x && N-35<y && N+35>y ) {
    X += (x - M)/6;
    Y += (y - N)/7;
    Z = 0.03;
  }
  if ( z<-1.3 ) x = y = z = X = Y = Z = 0.01;
}, 33);