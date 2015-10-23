var w = c.width = c.height=512,
  d = document,
  color= 0,
  level = 0,
  mode = 0,
  dragging=0,
  chars = "♥✿✵❄❀☮☯⚘☘✰",
  idl = w * w * 4,
  dx,dy,
  delta,
  symbol,
  x,y,q,
  id,
  shifted,
  newdata,
  ox, oy,
  px, py,
  startTime,
  time,
  i,
  t,
  amplitude,
  flashStart,
  nd;

function r() { return ~~((amplitude||w) * Math.random()); }

function ft2(e, f,ty, symbol, s,h,br) { 
  if (e) a.clearRect(0,0,w,w);

  a.font = s+"px Arial";
  a.fillStyle = "hsl("+h+",99%,"+br+"0%)";
  
  q = 9;
  while (q--)
    a.fillText(symbol, f + w*( q%3 -1),ty + w*(~~(q/3) -1) ); 
}

function match (e,f) {
  delta = (e - f) % w;
  return Math.abs(delta)<9 || w-delta<9;
}

function fun(e) { newdata[i+e] = id.data[ (idl + i + dx[e]*4 + dy[e]*w*4) % idl ]; }

function draw() {
  shifted = a.createImageData(w,w);
  newdata = shifted.data; 

  i=idl;
  while (i-=4)
  {
    fun(0);
    fun(1);
    fun(2);
    newdata[i+3] = 255;
  }
  a.putImageData(shifted,0,0);
}

function shift() { 
  dx=[r(id = a.getImageData(0,0,w,w)),r(),r()]; dy=[r(),r(),r()]; 
}

function createLevel() {
  i = level+1;

  while (i--)
    ft2(i==level,r(dragging = 0), w + r(startTime = nd), chars[level % 10], 9+r(), 0, 9); 

  shift(mode=1);
}

function textScreen(e, f) {
  ft2(1, 80,230,e,99,0,10);
  if (f) ft2(0, 99,270,f,30,0,10);
  shift(amplitude=20);
  amplitude=0;
}

d.onkeydown = d.onmouseup = d.onmousedown = d.onmousemove =  function(e) {
  t = e.type[5];

  px = e.pageX;
  py = e.pageY;

  if (t == "m" && dragging) 
  {
    dx[color] = (ox - px) % w;
    dy[color] = (oy - py) % w;
  }

  if (t == "d") {
    dragging=1; 
    ox = dx[color] + px;
    oy = dy[color] + py;
  }

  if (t == "u")
    dragging=0;

  if (t == "w") 
  {
    if (mode==1) {
      color++; color %= 3;
    }
    else
      createLevel();
  }
};

function loop() {
  if (mode==0) textScreen("RGB 1k", "@remcoder")
  if (mode==2) textScreen("# " + (level+1))
  // if (mode==3) textScreen("FAIL")

  nd = new Date;
  draw();
  
  if (mode==1)
  {
    time = 60 - (nd - startTime) / 1000;

    if (match(dx[0],dx[1]) && match(dy[0],dy[1]) && match(dx[1],dx[2]) && match(dy[1],dy[2]))
    {
      mode = 2;
      level++;
    }
    else if (time < 0)
      mode=0;
    
    ft2(0,20,60, ~~time, 60, color*120,8);
  }

  setTimeout(loop, 16);  
}

loop();