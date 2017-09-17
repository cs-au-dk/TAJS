/**
 * "Panzer Dragoon 1k" by @greweb
 * an entry for JS1K 2014 "Here Be Dragons"
 *
 * Blog Post: http://greweb.me/2014/03/panzer-dragoon-1k
 */

// window object is polluted like hell with one letter variables ;-)
// i, j, e, f : are temporary variables

// Game States

t = // Game frame current time.
g = // GameOver Time || 0 if no GameOver
D = // "Danger" variable: more it is high, more there is danger (means a lot of opponent are reaching the left part)
U = // mouseup flag. It represents a missile shoot request.
//x = y = // x,y: mouse position <- we don't need their declaration because the game can't be started without them
X = Y = // X,Y: player position
0;

// Q : the mob pop factor. More it is low, more there is opponent popping.
// Q ~ depends on t but in a non trivial way. it ~ follows a sawtooth curve. See the algorithm in the opponent creation code.
Q = 80; // We start a bit lower than the reset value.

// Here we create 2 collections, but those are used as objects not array! Each element of these is a vector
o = []; // an opponent: [ 0: x, 1: y, 2: health, 3: vx, 4: vy, 5: locked, 6: hitTime ]
p = []; // a particule: [ 0: x, 1: y, 2: size,   3: vx, 4: vy, 5: damage ]

// Game Input

//ontouchmove = // mobile support. I wish I had more bytes!
onmousemove = function (e) {
  x = e.pageX+99; // We don't directly use the mouse coordinate but we translate/scale the cursor to avoid behind able to shoot behind. It is like a constraint of the dragoon.
  y = e.pageY;
  t || L(); // the mouse moved, we can start the game once.
  //return e.touches ? ontouchmove(e.touches[0]) : !1;
}
//ontouchend =
onmouseup = function (e) {
  U = !g; // We can only shoot if game is not over
}

// Programmatically alias Context2d functions. (WARNING some functions are overrided with different behavior on different browsers)
for (e in c) c[e[0]+e[2]+(e[6]||"")] = c[e];

// L is the render loop
L = function (e) { with (c) { // with(c){} allows to we make all functions of "c" available in the scope

  // Call the next frame.
  setTimeout(L, 18 - 18 * x / a.width); // The framerate increases when you are targetting far in order to improve the gameplay.

  // Move player in a smooth way
  X = .98 * X + (x-X)/30;
  Y += (y-Y)/30;

  // Fire bullet
  ++t % 9 ||  // Increment the ticker here! but don't trigger a bullet each frame!
    g || // Don't trigger a bullet if game is over
    // Trigger one fire by adding to p
    (p[Math.random()] = [ // It is the f**king way to add an element! to be used with delete and for-in loops.
      X,
      Y,
      6,
      (x-X) / 30,
      (y-Y) / 30,
      2
    ]);

  t % Q < 1 && // May happen for a few times t until t%Q isn't < 1, it makes opponents coming by waves
    // Creates an opponent
    (o[Math.random()] = [
      // Following values are important part of game difficulty
      a.width, // X
      a.height * Math.random(), // Y
      // Health
      40+3e3*Math.random()/
        (
          Q = Q < 30 ? 200 : .98 * Q // Update Q <- decrease, and move back to initial value for each new level
        ),
      -a.width*(1+t/1e4+Math.random())/3e3, // VX
      0, // VY
      0, // hold
      0,
      "#1"+t%7+t%5 // Random color
    ]);

  // Save the state to be restored later
  sv();

  // Draw background
  globalAlpha = .8/(1+D); // The cheap way of doing Motion Blur

  fillStyle = strokeStyle = "#8cd";
  if (g) fillStyle = strokeStyle = "#e44";
  flc(0,0,a.width,a.height);

  // Next color for drawing bullets.
  fillStyle = strokeStyle = "#ffb";

  // Shake the whole screen proportionally to D
  taa(6*D*Math.random(), 6*D*Math.random());

  // Bullets Update & Draw
  for(j in p) { e = p[j]; // Foreach Bullet e
    // Drawing a circle
    bga();
    arc(
      // Update bullet position
      e[0] += e[3],
      e[1] += e[4],
      e[2],
      0, 9); // OMG trick to goes from 0 to 2π
    fl();
  }

  // Player Update & Draw
  fillStyle = strokeStyle = "#e44";
 
  // Drawing the Dragoon
  for(i=0,f=X; i<14; i++) {
    f -= 28 - 2*i + (i/20+.6)*(x-X)/30;
    flc(
      f,
      Math.cos(-i/2+t/12)*9 - i*(y-Y)/14 + Y - 14,
      28 - 2*i,// + 9*!i,
      28 - 2*i// + 9*!i
    );
    if (i == 3) flc(
      f,
      Math.cos(-i/2+t/12)*9 - i*(y-Y)/14 + Y,
      44+(x-X)/30,
      Math.cos(-i/2+t/12)*90
    );
  }
  
  D = x / a.width; // Danger is recomputed in the following Opponents loop
  // Opponents Update & Draw
  for(i in o) { e = o[i]; // Foreach Opponent e
    fillStyle = strokeStyle = "#ffb";
    if (t > e[6]) {
      fillStyle = strokeStyle = e[7];
    }

    // Drawing a circle (it is the same chunk of code previously used!)
    bga();
    arc(
      // Update opponent position
      e[0] += e[3],
      e[1] += e[4],
      e[2],
      0, 9);
    fl();

    f = [x, y, 18]; // Cursor "tuple", used for factorizing the distance formula

    // Cursor Targetting
    if (
      // Opponent is targetted if he was not yet and the cursor is near the opponent. e[5] becomes the time of the targetting
      e[5] = e[5] ||
        // cursor hits opponent
        (f[0]-e[0])*(f[0]-e[0])+(f[1]-e[1])*(f[1]-e[1]) < (f[2]+e[2])*(f[2]+e[2]) &&
        t
    ) {
      // Drawing a scaling/rotating cursor for the targetting effect
      sv();
      fillStyle = strokeStyle = "#ffb";
      taa(e[0], e[1]);
      if (0 < 20-(t-e[5])/2) {
        // From the targetting time, we scale down to make the targetting effect
        sa(
          20-(t-e[5])/2,
          20-(t-e[5])/2
        );
      }
      rt(-t/14);
      srR(-9,-9,18,18);
      rse();

      // Missile Bullet
      U && // If hold off and opponent targetted, trigger a missile to that opponent
        (p[Math.random()] = [
          X,
          Y,
          20,
          (e[0]-X)/20,
          (e[1]-Y)/20,
          40,
          e[5] = 0 // Also reset the "locked" state here.
        ]);
    }

    for(j in p) { f = p[j]; // Foreach Bullet f - N.B. it seems impossible to reconciliate to the previous for(i in p) loop here because there is better bytes save implied.
      if ( // bullet hits opponent
          (f[0]-e[0])*(f[0]-e[0])+(f[1]-e[1])*(f[1]-e[1]) < (f[2]+e[2])*(f[2]+e[2])
        // ^ Intentionally, we have the exact same code that that previously!
      ) {
        e[2] -= f[5]; // Apply bullet force from bullet size
        e[6] = t+5; // Flash effect
        e[0] += 9; // backwards effect on shot
        delete p[j]; // And here is the way we remove an element!
      }

      // Out of Bound to prevent memory leaks!!!
      if (a.width < f[0])
        delete p[j];
    }

    // Accumulate Danger with the left opponent distance
    D += Math.max(0, .6-e[0]/a.width);

    // Destroy Opponent if his life is enough small
    if (e[2]<18)
      delete o[i];
    // Game Over reached with an opponent reaches the left side
    if (e[0]<0)
      g = g || t;
  }
  
  // Draw the Cursor with its score text
  fillStyle = strokeStyle = "#ffb";
  taa(x, y);
  sa(2, 2);
  flx(g||t,18,18);
  rt(-t/14);
  srR(-9,-9,18,18);
  rse();

  // Draw the top-left Score
  sv();
  fillStyle = strokeStyle = "#ffb";
  if (g) sa(18, 18); // Scale it up if game is over
  flx(g||t,18,18);
  rse();
  
  U = 0; // reset the mouseup flag
}}