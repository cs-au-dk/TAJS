//Uses the technic for double maks appointed here: http://ir.gl/98bad9
w = window;
W = w.innerWidth;
H = w.innerHeight;
L = [];

for (k=-99;k<H;k++) {
  L[k] = [9e9,0];
}

c.width=W;
c.height=H;
c.style.width = W + 'px';
c.style.height = H + 'px';

B = "#FFF";
b = "#000";
S(a,B);
a.fillRect(0,0,W,H);

O = H/6;
s = "js1k";

// Create a canvas that we will use as a mask
function N() {
  M = document.createElement('canvas');
  // Ensure same dimensions
  M.width = W;
  M.height = H;
  m = M.getContext('2d');

  // This color is the one of the filled shape
  S(m,B);
  // Fill the mask
  m.fillRect(0, 0, W, H);
  m.globalCompositeOperation = 'xor';


  // heart
  m.beginPath();
  Z = W/2;
  h = H/3;
  P = Math.PI;
  m.moveTo(Z,H*.8);
  m.lineTo(Z-h-(h/7),h+(h/8));
  m.arc(Z-(h/2), h, h*.6,P-(P/8),0);
  m.arc(Z+(h/2),h,h*.6,P,P/8);
  m.closePath();
  m.fill();

  m.font = O+"px Arial";
  S(m,B);
  m.fillText(s,Z-((h*(s.length*.1))),h/.7);
}

N();

// Draw mask on the image, and done !
a.drawImage(M, 0, 0);


X=Y=0;


function I() {
  S(a,"#f00");
  for (k=0; k<10;k++) {

    D = Y-5+k;

    E = L[D];
    f = E[0];
    F = E[1];
    
    if (X-10 < f) {
      f = L[D][0] = X-5;
    }

    if (X+5 > F) {
      F = L[D][1] = X+5;
    }


    a.fillRect(f,D,F-f,1);
    a.fillRect(f,D,F-f,1);

  }
  a.drawImage(M, 0, 0);
  S(a,b);
  a.fillText("Move the cursor",Z,50);
}

setInterval(I, 10);

c.onmousemove = function(e) {
   X = e.clientX;
   Y = e.clientY;
}

w.onkeydown = function(e) {
  U = e.keyCode;
  if (U == 13)
    s ="";
  else if (U == 107)
    O++;
  else if (U == 109)
    O--;
  else
    s += String.fromCharCode(U);
  N(s);
}

function S(c,s) {
  c.fillStyle=s;
}