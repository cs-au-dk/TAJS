/*
 ____ __ __ ____ ____
/  __|  |  | ___/ ___| 
|  __|  |  |__  | __) 
|_|  `_____|____/____/  
 
FUSE - by @aemkei
Spread the love by absorbing all the positive energy around you.

In this simple game you control an energy ball through seven levels of love. 
The goal is to fuse with the warm glowing blobs. 
But beware: Others might grow to big and turn into negative energy.

Instructions

* click to accelerate movement
* collect warm (smaller) blobs
* avoid negative (bigger) energy
* fuse into a single giant ball
* master all seven levels

Features

* seven stages from easy to hard
* simple and unique game play
* smooth graphics, fonts and transitions

More info: http://j.mp/1kfuse

*/


/* 
 * CANVAS MAPPING
 * Reassign canvas methods to shortcut.
 */

for (
  i in a                              // find all properties in canvas
) a[

  i[3] + i[6]                         // create shortcut from 4. and 7. char
                                      // "fillRect"              => "lc"
                                      // "fillText"              => "lx"
                                      // "createRadialGradient"  => "aR"
                
] = a[i]                              // assign original property to shortcut


/* 
 * INITIAL SETUP
 * Create the canvas and setup game specific variables.
 */

B = L = 0                             // clear balls (B) and set level (L)

S =                                   // set screen size to 600 pixel
  c.height = 
  c.width = 
  600           

i = 320                               // set initial text position

/* 
 * NEW LEVEL
 * Update the level based on encoded data.
 */

n = function(e) {                     // next level
                                      // "e" is a placeholder here
                                      
  f =                                 // "f" is an empty string
    B = []                            // "B" is the array with all balls
                                      
  e =                                 // the final level data
     "WiDa7LLB`@`^?1WA@YAh8;rL1YKCEAxUCE?+AJuA{AI1W7@5C+K_CBmi=}>1MLB8UPMAV7CHFUUu9?1WKFu/iUI1uM?VKCABTAW-DWA1V_C2A9iCmAuiErAx7?rA<7?"

                                      // encoded level per line
                                      // "1" is uses as a delimiter
                                      // a char = two numberic values
                                      
    .split(1)[                        // convert string to array
      L                               // get current level as string
    ]                                 
                                      
  for (                               
    i = 0;                            // analyse level data
    i < e.length;                     
  ) f += e.charCodeAt(                // put numeric data into string
    i++                               // convert char into charcode
  ) - 32                              // shift down by 32 to get zeros
                                      
  for (                               
                                      
    i = 0;                            // analyse numeric level data
    f[i];                             // values range from 0 to 9
    P = B[0]                          // assign players ball ("P")
                                      
  ) B.push({                          // store ball data
    x: f[i++] * 60,                   // x position
    y: f[i++] * 60,                   // y position
    s: f[i++] * 10,                   // ball size
    X: f[i++] - 3,                    // x movement
    Y: f[i++] - 3                     // y movement
  })
}

/* 
 * MAIN LOOP
 * Update the stage in an interval.
 */

setInterval(function(e) {             // "e" is used as a placeholder
    
  if (!B) {                           // if there are no balls ...
    
    e = (                             // set alpha value for text
      i - 270
    ) / 30
    
    a.fillStyle = 'hsla(0,0%,0%,.2)'  // fadein black stage
    a.lc(0, 0, S, S)                  // "lc" -> fillRect
    
    a.font = '3em Impact'             // set font and text color
    a.fillStyle = 'hsla(' + new Date/10%360 +',100%,50%,' + (e-.3) + ')'

    a.lx(                             // "lx" -> fillText
      L < 8 ?   
        L ? 'ιεvει ' + L : '  fυsε' : // show level ...
          'τhε eηd',                  // or love if game is over
        
      240, 
      i-= (                           // move up text
        L < 8                         // .. only if we have an level
      )
    )
    
    e < 0 && n();                     // show next level if text was hidden
    return                            // cancel all other actions
  }
  
  a.fillStyle = 'hsla(0,0%,0%,.4)'  // fadein black stage
  a.lc(0, 0, S, S)                  // "lc" -> fillRect
  
  for (
    f = i = 0;                        // "f" counts the visible balls
    b = B[i++];                       // get a ball as long as there is one
  ) {
    
    with (b){                         // remember ball
    
      x < 0 ^ x > S && (X *= -1)      // switch x or y direction
      y < 0 ^ y > S && (Y *= -1)      // ... if ball is out of bounds
    
      x += X                          // set new position
      y += Y                          // based on current speed
    
      for (
        
        e = 0;
        l = B[e++];                   // compare with other balls
        
      ) l != b &                      // if they are not the same
        s > 0 & l.s > 0 && (          // ... and if they both have a size
                                      
      
          0 | Math.sqrt(              // convert to integer
            (x - l.x) * (x - l.x) +   // get distance
            (y - l.y) * (y - l.y)
          ) - s - l.s                 // ... and subtract the sizes
      
        ) < 0 && (                    // so if they touch ...
      
          l.s > s ? (                 // shrink the smaller
            l.s += .3,
            s   -= .3
          ) : (                       // inflate the bigger
            s   += .3,
            l.s -= .3
          )
        )
    
      s > 0 && (                      // if the ball has a size
      
        a.fillStyle = e = a.aR(       // prepare a new radial gradient
          x, y, 0,                    // at the ball's position
          x, y, s                     // with it's size
        ), 
      
        c = s > P.s ? 200 : 340,      // make it blue if creater than player
                                      // and add some fancy color stops
                                      
        e.addColorStop(               // fill players ball
          .4, 
          'hsla(' + c + ',100%,50%,' + (b==P)*1 + ')'
        ),
        
        e.addColorStop(               // pulsate players ball
          .5 + ((b==P) * .1 * Math.sin( new Date / 320 )), 
          'hsla(' + c + ',100%,50%,1)'
        ),
         
        e.addColorStop(
          .97,                        // fade ball to edge
          'hsla(' + c + ',100%,50%,0)'
        ), 
      
        a.lc(0, 0, S, S),             // call "fillRect" to draw the ball
      
        f++                           // increase count for visible balls
      )
    }
  }
  
  
  0 > P.s ? (                         // lose, if players ball if empty
    
    B = 0,                            // remove all balls
    i = 320                           // and move text to bottom
    
  ) : f == 1 && (                     // win, if only one ball is left
    
    L++,                              // increase level
    B = 0,                            // and reset balls and text position
    i = 320
    
  )
  
}, 30)


/*
 * MOUSE INTERACTION
 * Move ball based on clicked position.
 */

onmousedown = function(e) {           // watch mouse clicks
  P.X += (P.x - e.pageX) / 100,       // speed up ball
  P.Y += (P.y - e.pageY) / 100
}