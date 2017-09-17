C = [],
B = [],
O = Math,
b.bgColor = Z = 0,
T = .001,
onkeydown = onkeyup = function (a) {
  C[a.which - 32] = a.type[5]
},
setInterval(function () {
  for (Q = [], R = a.width |= d = 0; 4 > d;)    // clear the canvas ready for rendering
    with (
      B[d] = B[d] ||                               // get next object or....
      (W = d && 3 > d, L = U = V = M = 0) || {     // ...add object if it doesn't exist
      o: [                                            // object data
        "MTRWWOSOMCGOCOHWMT",                            // ship
        "MCFFCMFTMWTTWMTFMC",                            // orb
        "HkJiJ^DYFWMZTWVYP^PiRk",                        // orb plinth
        "CaCOLOMPMRNSUSSWOXO]Q]QZTYWSUQOQNPNO[O[a"       // map
      ][d],
      x: 210 * W,                                     // x position
      y: 1090 * W,                                    // y position
      r: 0,                                           // rotation
      u: 0,                                           // x velocity
      v: 0                                            // y velocity
    }) {
      P = B[0],                                       // store a reference to the player
      t = O.sin(r),                                   // pre-calculate sin (used later for direction and vertex transforms)
      q = O.cos(r),                                   // pre-calculate cos
      M || (                                          // is the player alive?
        d || (                                           // update player
          E = 5 * T * !! C[6],                              // increase thrust if 'up' key is pressed
          r += (!C[5] - !C[7]) / 50,                        // rotate if 'left' or 'right' keys are pressed
          u += U + t * E,                                   // increase players X velocity, adding U from orb movement if attached
          v += V - q * E + T,                               // increase players Y velocity, adding V from orb movement and gravity
          S = 2 + y * T                                     // zoom in as the player descends in to the caverns
        ),
        F = x,                                           // store object current X position
        G = y,                                           // store object current Y position
        x += u,                                          // move object along x axis
        y += v,                                          // move object along Y axis
        1 == d && (                                      // update the orb
          U = x - P.x,                                      // get X delta between ship and pod (used next iteration to adjust player)
          V = y - P.y,                                      // get Y delta between ship and pod
          J = O.sqrt(U * U + V * V),                        // get distance between ship and pod
          K = .5 * (J - 70) / J,                            // calc distance delta between ship and pod
          L && J > 68 ? (                                   // is the pod attached to the ship?
            r = O.atan2(V, U) - 1.5,                           // set rotation angle of pod (-1.5 is approx. Math.PI/2)
            x -= U *= K,                                          // move pod along X axis
            y -= V *= K,                                          // move pod along Y axis
            u = x - F,                                            // set pod X velocity
            v = y - G + T                                         // set pod Y velocity and add gravity
          ) : (                                             // if the pod isn't attached to the ship...
            L = O.abs(U) < 20 && 71 > J && !! C[8],            // capture it if the ship is directly above and 'down' key is pressed
            U = V = 0                                             // clear inertia values
          )
        )
      ),
      1 != L * d && c.beginPath();                    // start a new path unless the orb is attached to the ship (draws the arm automatically)
      for (f in o) {                                     // read the object vertex data (f%2==0 is X data, f%2==1 is Y data)
        if (Y = o.charCodeAt(f) - 77,                       // decode the coordinate
            d > 2 && (Y *= 70),                             // if this object is the map, scale it up by a factor of 70
            1 & f) {                                        // if this is the second vertex coordinate, we have an X and Y
          if (J = X * q - Y * t + x,                           // rotate and translate along the X axis
              K = Y * q + X * t + y,                           // rotate and translate along the Y axis
              d > L)                                           // if object is the ship (or orb, if attached) add vertex to the collision array
            for (j = 0; f > 1 && $ > j;)                          // check this line doesn't intersect any lines in the collision array...
              D = Q[j++],                                         // ...if it does, set the player dead flag (M==1)                                         
              E = Q[j++],
              F = Q[j],
              G = Q[j + 1],
              g = K - E,
              h = H - D, 
              i = I - E, 
              k = J - D,
              n = F - D,
              m = G - E,
              M |= g * h > i * k ^ (K - G) * (H - F) > (I - G) * (J - F) && i * n > m * h ^ g * n > m * k;
          else 
            r += M && M++,                              // simulate an explosion by rotating the ship / orb (M==0 when player is alive)
            $ = Q.push(J, K);                           // store these points in the collision array
          H = J,                                     // store end X data as the start of the next line
          I = K,                                     // store end Y data as the start of the next line
          c[f ? "lineTo" : "moveTo"](                // draw the line
            S * (J - P.x) + R / 2,
            S * (K - P.y) + a.height / 2
          )
        }
        X = Y                                      // if this is the first vertex coordinate, store it as X
      }
      A = 3 > d ? "stroke" : "fill",               // fill the ground, stroke everything else
      c[A + "Style"] = "#6c5",                     // set fill / stroke style for the object
      L ^ ++d && c[A]()                               // paint the object (if object is the ship with orb attached skip the paint)
    }
    B = M > 149 || P.y < -50 ? [] : B           // if the player escapes or crashes, reset the map
  }, 15)