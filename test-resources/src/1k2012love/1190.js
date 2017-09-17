var N = [], //background
    O = [], //objects
    i, // for looping through our context
    v = s = d = 0, // thrust, score, distance,
    h = c.height = 200, //playarea height
    w = c.width = 400, //playarea width
    y = g = h/2, //y position, g is game playing needs to be greater than 0 (playing) and 1 (so interval is not cleared)

    f = "fillStyle", // shorthand for fillStyle

    r = Math.random, //reassign

    p = "ǂ", q = "♥", // spike, heart
    j = '#fff', // white
    m = 'red', // red is shorter than #f00

    z = e = 3, //bg block size and difficulty start level

    S = function(A) { // font style (size)
      a.font = "bold "+A+"px arial";
    },
    L = function(A, B, C, D, E, F) { // loop game (array length, array item - both not passed in, C is symbol, D is whether to remove, E, F are not passed)
      
      c.width=w; // reset screen

      a[f] = '#300'; // background
      a.fc(0,0,w,h); // background

      D = w/z; // width of screen / blocks

      for (A=0;A<D*2;A++) // loop through bg object (forward so we don't replace blocks incorrectly)

        B = (A>=D) ? 2 : 1, // determine how far to move bg
        C = z*A-w*(B-1)-z, // set the x position to the correct co-ord based on the row we are on
        E = a.cL(-C,-80,w+50,h+50), // gradient vertical, horizontal
        E.a = E.addColorStop, // reassign
        E.a(0, (B>1) ? '#966' : '#633'),

        E.a(1, '#000'), // end color (black)
        a[f]=E, // set as fill style        
        a.fc(C,h,z,(-N[A]-120/B)), // draw on the screen
        N[A]=N[A+B]||0|r()*35; // set the bg to the next value to allow us to scroll. If a value doesn't exist, set to random.

      // end for loop

      A=O.length; // set A to the length of our object
      while (A--) { // loop backward through all our items
        S(35); // font size for hearts and spikes
        if (B = O[A]) { // assign B while we check O[A] exists
          E = B[0]-=e; // objects move left at the speed of difficulty
          F = B[1]; // y position

          // set up spike or heart
          C = B[2]; // set text C to object type
          a[f] = (C == p) ? j : m; // set color to white or red

          a.fx(C,E,F); // draw

          // test collision box - a.fc(15,y-22,20,40);
          if (E > 15 && E < 40 && y-22 < F && y+28 > F) // if in player's collision area
            (C == p) ? g=1 : s++, // if it's a spike, finish - else add score
            B[1]=0 // take object off screen to remove object (.splice is too expensive) - it's inefficient, but the game is short.
        }
      }

      // I use 25 and 15 in this loop a lot from here, so let's benefit
      E = 25,
      F = 15;

      // if the thrust is on
      //   if player is not at top of screen
      //     move player up, reduce thrust
      //   else do nothing
      // else 
      //   if player is not at bottom of screen
      //     move the player down the screen
      //   else kill player if y is greater than the screen height
      (v>0) ? ((y>E) ? y-= v-= .8 : v-= 3) : (y<h) ? y+=2 : g=1; 


      if (d%240==0) e++; // increase difficulty (e) every few meters
      if (d%(0|200/e)==0) O.push([w,0|r()*(h*.9)+E,(0|r()*2) ? q : p]); // create a new obstacle every 200/difficulty meters; add to our object array, random whether heart (q) or spike (p)

      S(F); // set font size to 15
      a[f]=j; // set fill style (text color) to white
      a.fx(s+q,w*.6,F); // output the number of hearts collected to the screen
      a.fx(d+'m',w*.7,F); // output distance to the screen

      if (g==1) // exit loop  - placed before rotate
        clearInterval(t),
        a.fx('='+(d*s),w*.6,h-F), // rating is distance * score - had to use = as couldn't afford 'score'
        a[f]=m; // make our character red

      d++; // increase the distance counter

      a.ta(0,y); // translate so rotate origin is correct :)
      a.rotate(.1); // isn't shortened by the context due to restore.

      S(E); // set font size to 25
      a.fx("C", E, -F); // cupid's head
      a.fx("U", E, 0); // body
      a.fx("P", E, F); // leg 1
      a.fx("I", 36, F); // leg 2
      a.fx("D", F, 0) // wings!
    };
// shrink context functions
//fillRect              fc
//fillStyle             fy
//fillText              fx
//createLinearGradient  cL
//translate             ta
//rotate                rotate (replaced by restore :()
for (i in a) a[i[0]+i[6]] = a[i];
// user interaction
b.onkeypress=function(){ // keypress as keydown doesn't repeat in Opera
  if(v<8)v+=3; // increase the velocity
  if(g>0)
    O = [],
    e = 3,
    y = h/2,
    s = d = g = 0,
    t=setInterval(L,33); // start loop: 33.3 is equivalent of 1000/30
};
L()