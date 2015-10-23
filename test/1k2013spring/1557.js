// triangleCanvas it a temporary canvas for creating mirror segments
var triangleCanvas = c.cloneNode();
var triangleContext = triangleCanvas.getContext('2d');

// Create canvases for plasma and mirror
var plasmaCanvas = c.cloneNode();
var plasmaContext = plasmaCanvas.getContext('2d');

var angle60 = Math.PI / 3,		// 60 degrees in raidans
  angle120 = angle60 * 2,		// 120 degrees in radians
  angle240 = angle120 * 2,		// 120 degrees in radians
  plasmaSize = c.width = c.height = 600,
  destA = 0,
  p1 = 0,
  p2 = 0,
  p3 = 0,
  p4 = 0,
  cdData,
  kaleidoSize = 400,
  kaleidoHalf = kaleidoSize/2,
  kaleidoHalfExtra = kaleidoHalf+1, // To avoid subpixel rendering artifacts
  destOffset = 100;

var t1, t2, t3, t4,
  aSin = [], 
  cd,
  x,
  idx,
  as = 1.2, fd = 0.4, as1 = 3.2, fd1 = 3.2, ps = -6, plasmaReverseFactor=.6;


var cd = plasmaContext.createImageData(plasmaSize, plasmaSize);
idx = 512;
while (idx--) {
  aSin[idx] = Math.sin(idx * Math.PI / 256) * 1E3;
}

var paintKal=function (b, c) {
  a.drawImage(
    triangleCanvas,
    0,
    0,
    kaleidoSize,
    kaleidoSize,
    destOffset + kaleidoHalf * (Math.cos(destA+b) + Math.cos(destA+c)),
    destOffset + kaleidoHalf * (Math.sin(destA+b) + Math.sin(destA+c)),
    kaleidoSize,
    kaleidoSize);
}

var paintPlasma=function (b, c) {
  // Build clip
  c += destA;
  triangleContext.restore();
  triangleContext.clearRect(0, 0, plasmaSize, plasmaSize);
  triangleContext.save();
  triangleContext.translate(kaleidoHalf, kaleidoHalf);
  triangleContext.beginPath();
  triangleContext.moveTo(kaleidoHalfExtra * Math.cos(c), kaleidoHalfExtra * Math.sin(c));
  triangleContext.lineTo(kaleidoHalfExtra * Math.cos(c + angle120), kaleidoHalfExtra * Math.sin(c + angle120) );
  triangleContext.lineTo(kaleidoHalfExtra * Math.cos(c + angle240), kaleidoHalfExtra * Math.sin(c + angle240));
  triangleContext.clip();
  // Paint plasma
  b += idx?destA*3:-destA;
  triangleContext.rotate(b);
  triangleContext.drawImage(
    plasmaCanvas,
    idx ? plasmaSize - kaleidoSize : 0,
    0,
    kaleidoSize,
    kaleidoSize,
    -kaleidoHalf,
    -kaleidoHalf,
    kaleidoSize,
    kaleidoSize);
}

var draw=function(b,c) {
// Begin plasma effect
cdData = cd.data;    
  
t4 = p4;
t3 = p3;

idx = plasmaSize*plasmaSize*4;
while (idx) {
  (idx%(plasmaSize*4))?0:(t1=p1,t2=p2,t3=t3+fd1&511,t4=t4+as1&511);
  t1=(t1+4)&511;
  t2=(t2+1)&511;
  x = aSin[t1] + aSin[t2] + aSin[t3] + aSin[t4];
   
  cdData[--idx] = 511;
  if (x>0) {
    cdData[--idx] = x/as;
    cdData[--idx] = x/ps;
    cdData[--idx] = x/fd;
  } else {	
    cdData[--idx] = Math.min(-x/as1,255)*plasmaReverseFactor;
    cdData[--idx] = Math.min(-x/ps,255)*plasmaReverseFactor;
    cdData[--idx] = Math.min(-x/fd1,255)*plasmaReverseFactor;
  }
}
plasmaContext.putImageData(cd, 0, 0);
p1 += ps;
p3 += ps;

  // Centre triangle
  paintPlasma(0,0);
  
  paintKal((Math.PI/4-destA), (5*Math.PI/4 - destA));

  // Triangle -120 degrees
  paintPlasma(angle240,0);

  paintKal(0, angle60);
  paintKal(angle120, Math.PI);
  paintKal(angle240, -angle60);

  // Triangle +120 degrees
  paintPlasma(angle120,0);

  paintKal(0, -angle60);
  paintKal(angle120, angle60);
  paintKal(angle240, Math.PI);


  // Mirror the newly generated plasma
  plasmaContext.save();
  plasmaContext.translate(plasmaSize, 0);
  // Set idx to 1 to indicate we're mirrowing
  plasmaContext.scale(-1, idx=1);
  plasmaContext.drawImage(plasmaCanvas, 0, 0);
  plasmaContext.restore();

  // First triangle
  paintPlasma(0,angle60);

  paintKal(angle240,angle120);
  paintKal(0, 0);

  // Second triangle
  paintPlasma(angle120,angle60);

  paintKal(0, angle120);
  paintKal(angle240, angle240);
  
  // Third triangle
  paintPlasma(angle240,angle60);

  paintKal(0, angle240);
  paintKal(angle120, angle120);

  destA += .01;
}

b.onclick = function(a,b){
  as = Math.random()*20/4;
  fd = Math.random()*20/2;
  as1 = Math.random()*20;
  ps = Math.random()*20-10;
  plasmaReverseFactor = Math.random()*20/10-1;
};
setInterval(function() { draw(); }, 70);