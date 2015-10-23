// zz85 / twitter.com/blurspline
// More details to come on my blog
// @ http://www.lab4games.net/zz85/blog/
//
// Minified by Closure, Packed with @Siorki's RegPack
// which improves @aivopaas's  JSCrush compression
//
// After going using a node script for my workflow,
// I tweaking some values in the minified form before submitting
// Therefore, this is probably not the final version,
// but probably you might find what you want if you
// have bothered to look in here..

var w, h // width, height
var image, data32; // data image stuff
var s; // section width
var down; // is button pressed
var f; // terrian function
var i, j; // loop counters
var u, v; // inner loop

var ballx, ballx2, bally, bally2; // ball positions
var ballay; // acceleration
var pany; // scrolling
var q, q2; // terrian height differences

var th; // base terrain height
var offx; // offfset
var u2, offy, v2, k; // variables in loops

var gbg; // gradient background

var score, airtime, comments; // some game play stuff..

// ball variables

c.width = w = 480;
c.height = h = 320;

s = 60;
airtime = down = 0

ballx = ballx2 = 100
bally = bally2 = s

// terrain function
f=function(a,b,c) {

  return Math.sin(a / 220  + seed) * 40 - Math.sin(a / s) * 32;

}

// different terrain when you refresh
var seed= Math.random() * 10;

// tweet i saw from @aivopaas on shortening canvas properties and functions
for(i in a)(function(i){a[i[0]+(i[6]||'')]=(''+a[i])[27]?a[i]:function(_){a[i]=_}})(i)


offx = 0; // offsetx
th = h / 2; // terrain height

// Use typed arrays for performance boost
image = a.createImageData(w, h / 2);
data32 = new Uint32Array(image.data.buffer);


a.ld(2)


// comments = ['yeah','great','awesome','wow!','moo', 'fantastic']
// comments = '♪♫❤☼';

setInterval(function() {

  a.clearRect(0, -s, w, h + s);
  offx = ballx - s;

  // BALL PHYSICS

  ballay = 0.4; // gravity

  // Position Verlet Swapping via Array trick !
  ballx = [2 * ballx - ballx2, ballx2=ballx][0]
  bally = [2 * bally - bally2 + ballay, bally2 = bally][0]

  // TERRIAN HEIGHTS
  q = f(ballx) + th;
  q2 = f(ballx + 1) + th;

  if (bally < q) {  // is ball on the ground?

    // in the air
    down&&(bally+=2)
    airtime+=0.05

    score = ''
    for (i=~~airtime;i--;) score += '✓', ballx += 0.004;

    a.fy('#fff');

    // if (bally > bally2) score += comments[~~airtime];


  } else {

    // on the hills

    if (q2 - q >= 0) {
      // is the slope going down?
      a.fy('#0f0');
      ballx += down ? 0.5 : 0.15;
      // score = down ? '☺' : '.'
      // score = 'ouch!' + airtime
    } else {
      // upwards
      a.fy('red');
      ballx -= down ? 0.5 : 0.15;
      bally += 0.5;

    }

    airtime = 0
    score = ''

  }

  ballx = Math.max(ballx, ballx2 - 4);
  bally = Math.min(f(ballx) + th, bally);


  // Generate the stripe pattern based on the terrian using a pixel approach
  for (u = w; u--;) {

    u2 = u + offx
    offy = f(u2) + 70

    for (v = h/2; v--;) {

      j = v * w + u;

      v2 = (v - offy) / (h - offy) * h; // h = th * 2 multiplier for slant (0..4)

      k = 1 - v2 / h;
      k = Math.max(k * k, 0.7) * 255;

      data32[j] =
        ((v < offy ? 0 : 255) << 24) | // alpha
        ((~~((u2 + v2) / s) % 2 // stripe?
            ? k : 0) << 16) | // blue
        (k << 8) | // green
        k; // red
    }
  }

  // Start to paint stuff

  pany = Math.max(s - bally, 0) * 0.3;

  a.save();

  a.translate(0, pany);

  a.putImageData(image, 0, 160 + pany);

  a.gC('destination-over') // thanks to this, we are painting things backwards

  // Draw ball
  a.beginPath();
  a.arc(ballx - offx, bally + s, 10, 0, 9);
  a.stroke();
  a.fill();

  a.fy('#fff');

  // Paint score ~~
  a.fillText(score, 30, 70);

  // Outline Terrian
  a.beginPath();
  for (i = w; i--;) a.lineTo(i, f(i + offx) + 229);
  a.stroke();

  // CLOUDS
  for (i = w; i--;) {

    // a.beginPath();
    i%9 || a.fillRect(Math.random()*w, Math.random()*h, 1, 1);

    a.gA(0.3)
    a.beginPath();

    a.arc(i * 12 - offx * 0.1 % 70 ,  (i%3)*5 + Math.random()*2, 20 + (i% 3)*5, 0, 9);

    a.fill();
    a.gA(1)
  }



  // We paint the background last
  gbg = a.createLinearGradient(0, -s, w, h + s)
  gbg.addColorStop(1, '#8DF')
  gbg.addColorStop(0, '#04B');
  a.fy(gbg);

  a.fillRect(0, -s, w, h + s);
  a.restore();


}, 30); // target 25 fps

onkeydown = onmousedown = function (a,b,c) {
  down = 1
}

onkeyup = onmouseup = function (a,b,c) {
  down = 0
}