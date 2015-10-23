/**
  Tank Duel
  (c) Florian Maul (@fmaul)

  Scorched Earth clone for two players.
  
  Gameplay:
  - Adjust your browser window size for terrain sizes
  - Press F5 until you have a desired terrain and tank placement
  - Use the left/right arrow keys to aim
  - Use number keys 1 (slow speed) to 9 (high speed) to fire a shot
  - Take turns until one tank gets hit
  - F5 for a new game

  Notes:
  - Spring theme? Look at the green hills ;-)
  - Built with custom variable replacement, uglifyjs and jscrush
  - Works in Chrome, FF, Opera, IE9
**/
with(Math) {
    b.style.margin =
    bulletx=            // set the bullet position to 0
    explt=explx=        // set the explosion timer to 0
    n=0;                // player n=0 for player 1 (red), n=4 for player 2 (green)
    trail={};           // records the bullet trail, keys are x coord, values y coord

    c.width = w = innerWidth;
    c.height = h = innerHeight-5; // -5 needed to disable scrollbars

    FindTerrain=function (l) {
        i = a.getImageData(l, 0, 1, h);
        for(y = 0; !i.data[ y * 4 + 1]; y++);   // increase y as long as the green channel == 0
        return y;
    };

    // fill background with blue
	a.fillStyle="#003";
	a.fillRect(0,0,w,h);
	
    // Create a green gradient for the landscape
    a.fillStyle=O=a.createLinearGradient(0,0,0,h);
    O.addColorStop(0.5, "#490");
    O.addColorStop(1, "#040");
	
	// Generate random mountain landscape
    (GenerateTerrain = function(l, terrainl, u, terrainu, diff) {
        if (l + 1 < u) {
            m = l+u >> 1; // get middle of the two bounds
                // add the bounds, devide by 2 and floor to int
            terrainm = abs((terrainl + terrainu) / 2 + (random() * diff) - diff / 2);
            GenerateTerrain(l, terrainl, m, terrainm, diff / 2); // recurse into the left half
            GenerateTerrain(m, terrainm, u, terrainu, diff / 2); // recurse into the right half
        }
        a.fillRect(l,h - terrainl,1,terrainl); // paint the land directly here, no additional for loop needed.
    })(0, h/3, w, h/3, h);

	// Initialize the player information
    player=[
		x=~~(30+random()*w/3),     // player 1 random x-position on the left
		                           // needs to be at least 30 from the edge, or explosion code will fail
		FindTerrain(x),            // player 1 y position depending on the terrain
		1,                         // player 1 angle of the turret
		"#f00",                    // player 1 color red
		i=~~(w-30-random()*w/3),   // player 2 random x-position on the right
		                           // needs to be at least 30 from the edge, or explosion code will fail
		FindTerrain(i),            // player 2 y position depending on the terrain
		-1,                        // player 2 angle of the turret
		"#0f0"                     // player 2 color light green
		
		// note: the player color are in channel R and G, whereas the background is on
		//       the B channel, which makes screen updates easier.
	];

    DrawTank = function(x,y,d,f) {
        // tank hit detection
        if (abs(x-explx) < 30 && abs(y-exply) < 30) {
            // announce the looser, announcing the winner would cost to much code
            a.fillStyle=f;
            a.fillText("Game Over", x, y-50);
            explt%=30;  // reset explosion timer, so it never reaches 30 and terminates
        }

        // cut whole around tank with blue sky color
        a.beginPath();
        a.fillStyle="#003";
        a.arc(x, y, 30, 3, 0);
        a.fill();

        // draw the tank with the players color
        a.beginPath();
        a.strokeStyle = a.fillStyle = f;
        a.arc(x, y, 10, 3, 0);
        a.fill();
        a.moveTo(x,y);
        a.lineTo(x + sin(d) * 20, y - cos(d) * 20);
        a.stroke();
    };

	// Game loop and event handling in one function
    setInterval(onkeydown=function(e) {
	
		// if a key was pressed ----------------------------
        if (e) {
		
            // handle key events
            S = e.keyCode % 48;                         // 48 is ascii of '0' -> keys '1' to '9' map to S=1 to 9
            if (S < 10) {                               // key 1-9 -> fire shot
                bulletx = player[n] + 4 * (             // calculate start position of bullet
                                                        // inlining the speed assignment saved 1 byte ;-)
                    bulletsx = sin(player[n+2]) * (4+S) // set bullet speed in x direction
                );
                bullety = player[n+1] + 4 * (
                    bulletsy = -cos(player[n+2]) * (4+S) // set bullet speed in y direction
                );

				// Reset the bullet trail currently on the screen
				// this is duplicated below, but compresses best if duplicated
                i = a.getImageData(0, 0, 1, 1);
                for(x in trail) a.putImageData(i, ~~x, trail[x]); // use x coord as integer to fix draw issue in IE/Opera
                trail = {};

				// advance to next player, alternate between 0 and 4 (XOR)
                n^=4;                          
            }
            else player[n+2] += (e.keyCode % 37 - 1) * 2  / 100;  // for keyCode 37, reduce angle by 0.02
                                                                  // for keyCode 39, increase angle by 0.02
        }
                                                              
        // draw tanks -----------------------------------
        DrawTank.apply(a,player);
        DrawTank.apply(a, player.slice(4));

        // draw projectile -----------------------------------
        if (bulletx) {
            bulletx += bulletsx;                          // bullet position x,y changed by bullet speed bulletsx, bulletsy
            bullety += bulletsy;
            bulletsy+=0.06;                              // accelerate towards the ground, aka gravity
            i = a.getImageData(bulletx, bullety, 1, 1);  // read the pixel at the bullets position.
            if (i.data[0]>60 || i.data[1]>60) {          // hit if we hit anything >mid red or >mid green, blue channel is ignored
                explx = bulletx;                         // start the explosion at the bullets position;
                exply = bullety;
                explt =                                  // start the explostion at time 0
                bulletx = 0;                             // stop the bullet flight;
            }
            else {
                i.data[0] = i.data[1] = 200; // draw the bullet 
                                             // only uses red and green channel, i.e. blue background is not effected
                a.putImageData(i, bulletx, trail[bulletx]=~~bullety);  // draw and assign the y coordinate to the trail history
				                                                       // save coords as integer to fix draw issue in IE/Opera
																	   // but use float index because there can be multiple entries per x-coord
            }
        }

        // draw explosion -----------------------------------
        if (explx) {
			// Reset the bullet trail currently on the screen
			// this is duplicated above, but compresses best if duplicated
			i = a.getImageData(0, 0, 1, 1);
			for(x in trail) a.putImageData(i, ~~x, trail[x]); // use x coord as integer to fix draw issue in IE/Opera
			trail = {};
		
            a.beginPath();
            a.arc(explx, exply, explt, 0, 6); // 6 = approx. Math.PI*2 ;-)
            a.fillStyle = explt++ < 30 ?      // if explosion timer < 30, meanwhile advance explision timer
                "#ff0" :                      // draw with yellow
                (explx = 0, "#003");          // else end explosion and draw with bg color
            a.fill();
        }

    }, 10); // end of game loop, every 10ms
}