// Roses Are Red by xpansive

// Compression procedure:
// Minify (all tools give pretty much the same output, if you use closure compiler put it on whitespace only mode and take the semicolon off the end)
// Crush with jscrush (http://iteral.com/jscrush)

// There are many bytes to be saved through the use of with statements, however on my machine it also drops the fps from 31 to 13, so I left them out.

// The code is heavily optimized for compression, for example (5-x)*z is smaller than z*(5-x) because )* is repeated a lot (you can't have it the other way around on function calls)

// I don't have a blog, so I tried to make the source as understandable as possible...

// I suggest you paste this into your favourite text editor, it's no fun reading it in a tiny text box... 

// Initalize some variables
// f = points in flower
// h = points in heart
// And remove canvas borders and scrollbars
// Style is capatalized to gain bytes after compression (to match fillStyle)
f=[h=[c.innerHTML="<Style>*{overflow:hidden;margin:0}"]];

// The object generation stage
for (x=-16;16>++x;S=new Date/999) // S holds the time since the demo started, set it here to save a semicolon
  for (y=-16;16>++y;m=onmousemove=function(_){o=_}) { // Set onmousemove for mouse interaction, it just copies the event to global o
                                                      // Also set m as shorthand, this function also stores the morphed 3d points (you can use a function like an array)
                                                      
    // Generate the heart, math taken out of iq's ShaderToy and crushed a bit math wise
    for (z=-5;z++<4;
      ((22-10*o)*o-13)*o/(7*o-9)>Math.sqrt((x*x+y*y)/676+z*z/128)&& // If the heart intersects this point
        f.push({x:Math.random()+x,y:Math.random()+y,z:Math.random()+z})) // Add the point to the list plus random offsets of 0 to 1
      o=Math.abs(Math.atan2(x,y)/Math.PI); // This is used in the calculations for the heart, this code runs first to set the variable

    // Generate the flower, math by myself through trial and error
    for (z=-6;z++<45;
      Math.abs(x+Math.sin(Z=z/6))+Math.abs(y+Math.sin(Z))<1.5 // The stem
        &Z>0 // No stem if z<0
        |1>Math.sqrt((x*x+y*y)/49+Z*Z*Z)&& // The head
          h.push({x:Math.random()+x,y:Math.random()+y,z:Math.random()+z})) // Add the point
      o={pageX:0,pageY:0} // Use the free semicolon to set a default value for the mouse rotation offset
  }
setInterval(function(_) { // The main draw loop. _ is unused; just here to match the other function sig (for jscrush savings)
  t=new Date/999-S, // Set t to current time - demo start time
  a.fillRect(0,0,W=c.width=innerWidth,H=c.height=innerHeight), // Maximize the canvas, clearing all state at the same time (which sets fillStyle to black) then fill a rectangle covering the screen
  a.font="italic 1cm x"; // Set the font used for the text, 1cm is smaller than something like 20px

  // The point drawing loop
  // i is our counter, 1696 is the number of points in each object
  // w is the object morph offset, set z to whether that has changed since the last frame
  // Only draw every second point for speed reasons
  for(i=1696,z=w!=(w=Math.max(0,Math.min(1,.5-3*Math.sin(t/8))));i-=2;) {

    // Morph the objects together and store it in m (doubles as onmousemove function)
    // If z, clear the current morphed point (m[i]) and iterate through all if the axes (members) of the current flower point, could also use heart point
    // We clear m[i] this way to save a byte, we type [i] a lot so this is shorter than {} or []
    // If z is false, we just iterate over false, which has no properties, and therefore this does nothing at all
    for(j in z&&(m[i]=[i],f[i]))
      m[i][j]=(f[i][j]-h[i][j])*w+h[i][j]; // Morph the axis based on w (just do simple linear interpolation)

    // m[i][j] is used over m[i].z in the next section because it saves some bytes when crushed, due to repitition of [j]. (j = 'z' after the previous code runs)
    // do the beginPath (required for circles) and use the free semicolon to set the color of this point
    // Most of this line is for the code to turn it blue
    a.beginPath(a.fillStyle="hsla("+[(6<m[i][j])*140-20-(m[i][j]<6)*Math.max(0,Math.min(1,2.5-Math.abs(6-t/Math.PI%16)))*99-Math.sin(t)*32,"97%,47%,.2)"]),
    
    // Rotate the point in 3D and draw the circle
    l=Math.sin(Z=t+3*o.pageY/H)*m[i].y+Math.cos(Z)*m[i][j], // Used in 3D rotation calc
    a.fill(a.arc( // Stick arc in the free semicolon zone
      (Math.sin(y=.7*t+3*o.pageX/W)*l+Math.cos(y)*m[i].x)*25+W/2, // Point x
      H/2+(Math.cos(Z)*m[i].y-Math.sin(Z)*m[i][j])*25, // Point y
      Math.max(0,12-(Math.cos(y)*l-Math.sin(y)*m[i].x)/2.5), // Point size/z
      Math.max(0,6-t),6)), // Controls the fade in at the beginning

    // Set the text color. Its smaller to use rgba here at first glance, but hsla saves _lots_ of bytes because it lets us repeat stuff, and jscrush likes that
    // Use .51 because with .5 we could get numbers in scientific notation, which hsla doesn't allow
    a.fillStyle="hsla("+[332,"97%,47%",.51-Math.cos(t/2)/2]+")",
    // Draw in the flowers/hearts floating around
    i%41||a.fillText("❁✿♡❤"[3&t/4/Math.PI],W/2+Math.sin(t/8+i)*W/2,H/2+Math.cos(t/8+i*9)*Math.sin(t/2)*H/2)
  }
  // Finally, draw in the poem itself!
  a.fillText(["roses are red","violets are blue","i ♥ js1k","and so should you"][3&t/4/Math.PI],200+W/2,H/2+Math.pow(7*Math.cos(t/4),3))
}, w=16) // Aim for 16 fps, also define w (morph offset, value doesn't matter yet)