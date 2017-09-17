/** 
 * Thanks to
 *
 * W3 Schools http://www.w3schools.com
 * Stack Overflow http://stackoverflow.com/
 * Closure Compile http://closure-compiler.appspot.com/home
 * JS Crush http://www.iteral.com/jscrush/
 *
 * This code was compressed by the latter two to fit in the 1024 bytes size limit
 */

/** The vertices of the dragon polygon (right side) [x0,y0, x1,y1, ..., xn,yn] */
var dragonPolygon = [37,0, 32,5, 22,3, 38,20, 28,45, 6,66, 17,33, 5,6, -5,7, -9,3, -37,0];
/** The time per level */
var time = 3600;
/** The number of dragons (including Snogard) */
var dragons = 1;
/** X coordinate of mouse pointer */
var targetX;
/** Y coordinate of mouse pointer */
var targetY;
/** Health of Snogard */
var snogardHealth;
/** X coordinates of dragons */
var dragonX = [99, -99, -99, -99, -99, -99, -99];
/** Y coordinates of dragons */
var dragonY = [99, -99, -99, -99, -99, -99, -99];
/** Direction of dragons in radians */
var dragonZ = [3, 3, 3, 3, 3, 3, 3];
/** Fire variable of dragons */
var dragonFire = [3, 3, 3, 3, 3, 3, 3];
/** X coordinate of the egg */
var eggX;
/** Y coordinate of the egg */
var eggY;

/** Animate function, includes almost all logic */
function animate() {
    // Canvas, Canvas Context, Math
    with(a) with(c) with(Math) {
    
        // Check if another minute has elapsed, increment time, 3593 to provide smooth flapping at level up
        if (time++ > 3593) {
            // Clear time
            time = 0;
            // Power up for Snogard
            snogardHealth = eggY ? 0 : 99;
            // Increment chasers
            dragons++;
            // Generate X coordinate of the Egg
            eggX = random() * (width - 9);
            // Generate Y coordinate of the Egg
            eggY = 9 + random() * (height - 9);
        }
        
        // Clear screen with green
        fillStyle = "#080";
        fillRect(0, 0, width, height);
        
        // Set darker green to stroke blades of grass
        strokeStyle = "#040";
        
        // Stroke blades of grass, positions based on the polygon of dragon (only seems to be random), it's something
        for(i in dragonPolygon) {
            strokeText("\\l/", width / 48 * abs(dragonPolygon[i]), height / 42 * abs(dragonPolygon[21 - i]));
        }
        
        // Draw egg, use "tan" color (almost white), use asterisk character (very small, but somewhat filled and round)
        strokeStyle = "tan";
        strokeText("*", eggX, eggY);
        
        // Pick up egg
        if (abs(dragonX[0] - eggX) + abs(dragonY[0] - eggY) < 60) {
            // Hide it, by putting it on the edge of canvas
            eggY = 0;
        }

        // Display time, set the "fire" strokeStyle now
        strokeStyle = "#e93";
        strokeText(~~(time / 60), 110, 9);
        
        // Update variables, draw dragons, fire and indicators
        for(i = 0; i < dragons; i++) {
            // Get the relative position of the dragon's target (Snogard's target is the mouse pointer, the dragons' target is Snogard)
            var dx = dragonX[i] - (i ? dragonX[0] : targetX);
            var dy = dragonY[i] - (i ? dragonY[0] : targetY);
            // Find the angle diference in radians between the dragon's angle and the relative angle of the position of its target
            var diff = atan(dy / dx) + ((dx < 0) ? PI : 0) - dragonZ[i];
            // Normalize diff to (-PI, PI) interval
            while (abs(diff += diff > 0 ? -2 * PI : 2 * PI) > PI) {
                // Normalization is done in condition
            }
            
            // Attack Snogard based on the dragon's angle and relative position, and Snogard's fire
            if ((abs(diff) < 1) & (abs(dx) + abs(dy) < 70) & i & (dragonFire[0] < 50)) {
                dragonFire[i] = 99;
                snogardHealth--;
            }
            
            // Rotate dragon by random steps, this way the gameplay is less deterministic
            dragonZ[i] += (diff < 0 ? -0.02 : 0.02) * (random() + 0.1);
            
            // Snogard's color is "tan", the others are "red" (the only 3 letter html color codes :)
            fillStyle = i ? "red" : "tan";
            
            // Save the context state (transformation, shadow, lineWidth)
            save();
            
            // Set Dragon Shadow
            shadowColor = "#000";
            shadowBlur = shadowOffsetX = shadowOffsetY = 30;
            
            // Translate (Move dragon, newer dragons are faster), rotate
            translate(dragonX[i] -= cos(dragonZ[i]) * (2 + i / 9), dragonY[i] -= sin(dragonZ[i]) * (2 + i / 9));
            rotate(dragonZ[i] + PI);
            
            // Draw Dragon Polygon
            beginPath();
            // First one side, then the other mirrored side, tricky way to get first k = -1, then k = 1
            for(k = -2; k++ < 1;) {
                // Go through the vertices of the polygon
                for(j = 0; j < 22; j += 2) {
                    // Draw body and Flapping wings, inside condition: (j > 7) & (j < 16) is for flapping wings
                    lineTo(dragonPolygon[j], k * dragonPolygon[j + 1] * (0.8 + 0.2 * ((j % 16 > 7) ? sin(time / 26 + i) : 1)));
                }
            }
            
            // Fill Dragon Polygon
            fill();
            
            // Hide shadow of fire by high blur, takes less code
            shadowBlur = 99;
            
            // Draw fire with bold tilde characters, lineWidth = 3
            if (dragonFire[i]-- > 0) {
                strokeText("~~~", 38, lineWidth = 3);
            }
            
            // Restore the context state (transformation, shadow, lineWidth)
            restore()
            
            // Draw Health, and number of Chaser dragons
            fillRect(5, i * 9 + 5, i ? 5 : snogardHealth, 5);
        }
        
        // Check if Snogard is still alive, or survived all dragons, otherwise continue
        if (snogardHealth <= 0) {
            // Lose
            alert(":(");
        }
        else if (dragons > 7) {
            // Win
            alert(":D");
        }
        else {
            // Continue game
            requestAnimationFrame(animate);
        }
    }
};

// Add MouseMove Listener
onmousemove = function (e) {
    // Get the coordinates of the Mouse Pointer
    targetX = e.pageX;
    targetY = e.pageY;
};

// Add MouseUp Listener
onmouseup = function (e) {
    // Start breathing fire
    if (dragonFire[0] < 0) {
        dragonFire[0] = 99;
    }
};

// Start game
animate();