// TAKE OVER THAT CANVAS
// c = canvas context
d = document;

// ZOOM FACTOR
// Z=3;

// STEP SIZE && ZOOM FACTOR
// z = 3;

W = a.width  / 3;
H = a.height / 3;

c.scale(3,3);

// KEY PRESSED
k = 0;

//MMM.. COLORS
//JUNGLE GREEN 2ABB9B
g = '#3BA';
// YELLOW F5C959
// ITS GONNA BE USED DIRECTLY
// w = '#FD6'; 
// CINNABAR E74C3C
r = '#E54';

// DELTATIMES
N = Date.now;
pN = N();

// RANDOMMMMM!
R = Math.random;

// SCORE
s=0;
// LIVES
v=5;

// CONSTANTS
T=10;

// INIT
x=T, y=H/2;

// fillText shortcut
c.t = c.fillText;

// ENEMIES
e = new Array();

// BULLETS
b = new Array();

// context.fillStyle function
fs=function(l){c.fillStyle=l;}

// DRAW RECTANGLE
dd=function(o,l, w,h) {
    fs(l);
    c.fillRect(o.x, o.y, w||o.w, h||o.w);
};

// COLLITION
co=function(o,d,Q){
  for (var i=0;i<d.length;i++) {
    var m = d[i];
    if ( (o.x+o.w >= m.x) && 
         (o.x <= m.x+m.w) &&
         (o.y+o.w >= m.y) &&
         (o.y <= m.y+m.w))
      { d.splice(i,1);
        // another nasty lenghtsaver
        // send a true 3rd param to increment the score
        s+=Q;
      return true; }
  }
  return false;
}

// CREATE RECTANGLE
nr=function(x,y,w,l){
  return new Object ({x:x || W+R()*150,
                      y:y || R()*H, 
                      i:0,
                      w:w || T,
                      l:0 || l
                    });
}


// nasty reuse of key to be the index in the loop to fill enemies
while(k<T) {
  e.push(nr());
  k++;
}
console.log(k);
console.log(e.length);

// GAMELOOP
gl=function() {
    requestAnimationFrame(gl);
    dd(({x:0,y:0}),'#666',W,H);

    if (v<1) {
      fs(r);
      c.t("score: "+s, T, T);
      return
    }


// DELTA TIME
    D = (N()-pN) / 100;
    pN = N();

// SCORE
    // fs(g);
    dd(({x:5,y:5}), g, 5,5);
    c.t(v, T, T);
    // fs(r);
    dd(({x:5,y:15}), r, 5,5);
    c.t(s, T, 20);

// PLAYER
    if (k!=0){
      if (k==40) y += 3;
      if (k==38) y -= 3;
      // to shoot: create bullet and reset keypressed
      if (k==32) { b.push(nr(x,y,5)); k=0;}
    }

    // PLAYER BOUNCE WITH THE UPPER/LOWER LIMITS
    if (y<0) y=0;
    if (y+T>H) y=H-T;

    // collition between player and enemies group
    var P = ({x:x,y:y,w:T});
    if (co(P,e,0)) v--;

    dd(P, g);

// ENEMIES
    for (var i=0; i<e.length; i++) {
      var m = e[i];
      m.y -= 3*Math.cos(m.i*D);
      m.x -= 3;

      m.i++;

      // if enemy passed behind or it collides with a bullet, reneweit
      if (m.x<0 || co(m,b,1))  
        e[i]=nr();

      dd(m, r);
    }

// BULLETS
    for (var i=0; i<b.length; i++) {
      var m = b[i];
      m.x += 3;

      // if a bullet passed away, removeit
      if (m.x>W) b.splice(i,1);
      
      dd(m, '#FD6');
    }
    
};
gl();


//KEYBOARD HOOKS 
d.onkeydown = function(e) {
    k = e.keyCode;
}
d.onkeyup = function() {
    k = 0;
}