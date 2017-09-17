L="♥", //Our little runner, celebration animation and placeholder for an empty set of possible maze generation steps
q=z=h=21, // q = current font size, z = width and generic constant, h = animation counter
b="#fff3#ffb3#4df3hsla(0,100%,84%,.1)".split(3), // All the colours we need, the hsla is pretty but tricky to shorten
o=setTimeout; // shorthand
c.width=c.height=441; // set the size of the canvas (z^2)
i=function(d,f,g){ // The 'paint' function. d = colour index (in b above), (f, g) = (x, y) position to draw
    with(a)            // to save having to write out 'a.' each time
        textAlign="center",     // Set the properties of the text on the canvas...
        textBaseline="middle",  // ...these are annoyingly long.
        font=q+"px a",          // Sneaky trick: When setting the font size, you must also set the font family
                                // but rather than set 'Arial' (5 bytes), this tries to set a font called 'a' (1 byte).
                                // As that isn't available, it falls back to the system default
        fillStyle=b[d],         // Set the colour using the array and index
        fillRect(z*f,z*g,z,z),  // Draw the current square
        fillStyle="red",        // Set the colour for the runner and the animation
        d||fillText(L,z*f+10,z*g+11) // Handy Evaluation Shortcut: 
                                     // the right-hand side of the OR is only evaluated if d is falsy (in this case 0)
                                     // i.e. "only draw the text if this is the runner"
};

s=function(){                       // Our 'Solver' function
    n[y][x]=3;                      // Set previous step as 'visited'
    2>n[y+1][x]?                    // If this direction (South) is free
        (n[++y][x]=1,k.push(y,x)):  // Go here
    2>n[y-1][x]?                    // Repeat for north
        (n[--y][x]=1,k.push(y,x)):
    2>n[y][x-1]?                    // Repeat for west
        (n[y][--x]=1,k.push(y,x)):
    2>n[y][x+1]?                    // Repeat for east
        (n[y][++x]=1,k.push(y,x)):
    (x=k.pop(),y=k.pop());          // If there is no way forward from here, backtrack
    
    n[y][x]=0;                    // Move the runner here
    19==y&&19==x?                 // If we've found our way to the goal
        e(h=z):                   //         Run the celebration animation
    o(s,50);                      // otherwise, setTimeout for our next animation/solution frame
    for(r in n)                   // For each row,
        for(l in n[r])            // For each column,
            i(n[r][l],l,r)        // Paint the square
};

(e=function(){                // The combination celebration and maze generation function
    
    if(h--)                   // if there are steps left to complete in the animation
        q=z*(z-h),            // Set the font size to get bigger
        i(0,10,10),           // Draw the celebration heart in the middle
        q=z,                  // Reset the original font size (for the trails)
        o(e,40);              // setTimeout on this
        
    else{                     // If we're not animating, generate a maze
        k=[];                 // Empty array to keep track of where we've been
        for(n=[],y=z;0<y--;){ // Loop through the existing maze and set everything back to being wall
            n[y]=[];
            for(x=z;0<x--;)
                n[y][x]=2
        }
        x=y=1;                        // Start top left
        n[y][x]=0;                    // This is us
        for(k.push(y,x);k.length;)    // Mark this square as corridor then loop while there is more corridor to carve
            (                        
            p=L,                        // Our stack of potential directions. To save space, this is done as a string
                                        // but in order to be able to use numbers as directions, we set p to a string so we can concatenate
                                        // We use steps of two for the maze so we can draw chunky walls
            18>y&&2==n[y+2][x]&&(p+=1), // If we aren't at the bottom edge and we're looking at a wall, South is a valid direction
            2<=y&&2==n[y-2][x]&&(p+=2), // Repeat for North
            2<=x&&2==n[y][x-2]&&(p+=3), // Repeat for West
            2==n[y][x+2]&&(p+=4),       // Repeat for East
            p!=L)?                        // While there are potential directions (i.e. our stack contains something)
                (
                1==(W=p[~~(Math.random()*p.length)])?   // Pick a random direction, if it is South
                    n[++y][x]=n[++y][x]=1:              // clear the two squares south of here 
                2==W?                                   // Repeat for North
                    n[--y][x]=n[--y][x]=1:              
                3==W?                                   // Repeat for West
                    n[y][--x]=n[y][--x]=1:              
                4==W&&(n[y][++x]=n[y][++x]=1),          // Repeat for East
                
                k.push(y,x)                             // Add this to the stack of places we've been
                ):  
                    (x=k.pop(),y=k.pop());              // Once we've got gone as far as we can, work our way back to continue generating
                    n[19][19]=0;                        // Put the goal at the bottom right
                    s(y=x=1)                            // Start solving
    }
})()    // Kick it all off by running the animation