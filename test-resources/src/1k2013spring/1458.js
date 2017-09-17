// MINIFICATION:
// ************
// - 1. make closure
//      Google Closure Compiler in ADVANCED mode
// - 2. Replace spaces in " in " by tabs
//      If not, jscrush will crash...
// - 3. Crush the code with jscrush
//      http://www.iteral.com/jscrush/

// CONSTANTS:
// **********
A = 9;     // RADIUS
B = R =50; // B: SPRING LENGTH
           // R: MOUSE AREA RADIUS
C = 0.01;  // SPRING "K" VALUE
D = 0.8;   // INERTIA
E = 15;    // MAX DISPLACEMENT (HALF MANHATTAN DISTANCE)
X = 0;     // CENTER X
Y = -100;  // CENTER Y
V = {};    // BALLS

S = T = 0; // S is true if the space bar is activated
           // T is true if the user has just clicked

M = Math; // Math package

// Also, local variables:
// **********************
// d: distance
// e: dx
// f: dy
// ***
// r: mouse x
// s: mouse y
// ***
// k: minimalX
// l: minimalY
// m: maximalX
// n: maximalY
// ***
// i, j: iterator
// ***
// q, t: objects
// ***
// o, p: balls

// Add three balls:
for (I = 0; I < 4; I++)
  V[I] = [
    // Position
    I,
    - I * 4,

    // Displacement
    0,
    0,

    // Links:
    {},
    !I
  ];

// Connect the three balls
V[2][4][1] = V[3][4][1] = V[3][4][2] = 1;

// ROLLOVER MANAGEMENT:
// ********************
c.onmousemove = function(v) {
  V[0][0] = v.pageX - 5 + X;
  V[0][1] = v.pageY - 5 + Y;
}

// CLICK MANAGEMENT:
// *****************
c.onclick = function(v) {
  V[0][0] = v.pageX - 5 + X;
  V[0][1] = v.pageY - 5 + Y;
  T = 1;
}

// CTRL MANAGEMENT:
// ****************
b.onkeydown = function(v) {
  S = S || v.which == 32;
}

b.onkeyup = function(v) {
  S = v.which == 32 ? 0 : S;
  R = R + ((v.which == 80) - (v.which == 77)) * 5 || 5;
}

setInterval(function(v) {
  // Resize + clear the canvas
  c.width = c.height = 600;

  // Init camera vars:
  k = m = l = n = 0;

  q = {}; // If spacebar: Balls to connect, else: Balls to delete

  for (i in V) {
    o = V[i];

    if (+i) {
      o[2] = M.max(M.min(o[2], E), -E);
      o[3] = M.max(M.min(o[3], E), -E);

      // Apply forces:
      o[0] += o[2] || 0;
      o[1] += o[3] || 0;

      // Collision with the ground:
      o[1] = M.min(-A, o[1]);
    }

    a.fillStyle = +i ? '#000' : S ? '#f99' : '#9cf';

    // Draw balls:
    a.beginPath();
    a.arc(o[0] - X, o[1] - Y, +i ? A : R, 0, 6);
    a.closePath();
    a.fill();
    
    a.fillStyle = '#000';
    for (j in o[4]) {
      p = V[j];

      a.beginPath();
      a.moveTo(o[0] - X, o[1] - Y);
      a.lineTo(p[0] - X, p[1] - Y);
      a.closePath();
      a.stroke();
    }

    // FORCES:
    // *******
    // - Gravitation
    // - Springs
    // - Intertia
    // - Collision with the ground
    // - Collision with between V
    // Init:
    if (+i) {
      o = V[i];
      o[2] *= D;
      o[3] *= D;
      
      // Gravity
      o[3] += 1.5;
    
      for (j in V) {
        if (T && S && q[j]) {
          delete o[4][j];
        } else if (+j < i) {
          p = V[j];
          e = p[0] - o[0];
          f = p[1] - o[1];
          d = M.sqrt(e * e + f * f);

          v = (+j && d < 2 * A ? (2 * A - d) / d / 2 : 0) // Collision between o and p
              - (o[4][j] ? C * (d - B) : 0);              // Spring force
              
          o[2] -= e * v;
          o[3] -= f * v;
          p[2] += e * v;
          p[3] += f * v;

          // Space bar management:
          if (!+j && T && d < R) {
            q[i] = 1;
          }
        }
      }

      // Camera stuff
      k = M.min(o[0], k || o[0]);
      l = M.min(o[1], l || o[1]);
      m = M.max(o[0], m || o[0]);
      n = M.max(o[1], n || o[1]);
    }
  }

  // Manage click:
  if (T) {
    if (S) {
      for (i in q) {
        delete V[i];
      }
    } else {
      V[I++] = [
        V[0][0],
        V[0][1],
        0,
        0,
        q
      ];
    }
  }
  T = 0;

  X = (k + m - c.width) / 2;
  Y = (l + n - c.height) / 2;

  // Draw ground:
  a.fillRect(0, -Y, 600, 600);
  a.fillStyle = '#fff';
  a.fillRect(0, 2 - Y, 600, 600);
}, 20);