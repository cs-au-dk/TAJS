// Burninating the Countryside!
// A tribute to my favorite dragon, Trogdor the Burninator.
// I'm randomly generating a background "countryside" with various greens,
// then drawing the char and flames on top. The flames follow Game of Life 
// rules, with some added complexity to add "embers" where fires once were
// and charred areas of increasing darkness once the embers die out.

// Global variables used throughout.
var	_loop = 0,
// _generation = 0,
squares = 80,
sizeX,
sizeY,
charLevels,
fireLevels,
emberLevels,
fire,
charred,
landscape,

// Initialize our demo.
init = function() {
	setScale();
	generateLandscape();
	seed();
	charred = generateEmpty(squares, squares);
	loop();
},

// The game loop. Triggers update and draw.
loop = function() {

	// Only update/draw every third frame. Otherwise it looks too fast.
	if (_loop % 3 == 0) {
		update();
		draw();
	}

	_loop = requestAnimationFrame(loop);
},

// Update our arrays. The meat of the demo.
update = function() {		

	// Our fire locations on the previous frame will be used to determine our current one.
	var w = fire;

	// Empty out our arrays.
	fire = generateEmpty(squares, squares);
	charLevels = generateEmpty(11, 0);
	fireLevels = generateEmpty(4, 0);
	emberLevels = generateEmpty(5, 0);

	// Look at each cell of the old frame and determine the new frame according to a set of rules.
	// Based on Conway's Game of Life with added rules for decaying embers and charred areas.
	eachCell(function(y, x) {

		var z = w[y][x], // the current cell in last iteration.
			n = nearFire(w, x, y); // number of fires near our current cell.

		// Current fires: burn on or burn out?
		if (z > 1) {

			// Burn out. Become an ember.
			if (n < 2 || n > 3) {
				fire[y][x] = -1;
			}

			// Burn on!
			else {
				fire[y][x] = n;
			}
		} 

		// If we have fire on three sides, ignite!
		else if (n == 3) {
			fire[y][x] = n;
		} 

		// Embers: Continue to smoldering or turn to char?
		else if (z < 0) {

			// Keep smoldering.
			if (--z > -emberLevels.length) {
				fire[y][x] = z;
			}

			// Burned out. Turn to char if we're past our initial seed time.
			// (i.e., flames that only existed for 1-2 draws afer seed don't count)
			else if (_loop > 30) {
				charred[y][x]++
			}

		}

		// Sort our char into color-based levels for easier drawing.
		if (charred[y][x] > 0) {
			charLevels[charred[y][x] < 10 ? charred[y][x] : 10].push([x, y]);
		}

		// // Sort active fires into levels.
		if (fire[y][x] > 1) {
			fireLevels[fire[y][x]].push([x, y]);
		}

		// // Sort embers into levels.
		else if(fire[y][x] < 0) {
			emberLevels[Math.abs(fire[y][x])].push([x, y]);
		}

	});

},

// Draw the resultant frame to the screen.
draw = function() {

	// Draw the landscape. Effectively clears the frame as an added bonus.
	c.drawImage(landscape, 0, 0);

	// Draw charred areas on top of our landscape.
	// Char is always black, with an opacity determined by the number in the char array.
	// Max opacity is 10/11, to allow the variation in the landscape to show through.
	for(var lvl = 1; lvl < charLevels.length; lvl++) {
		c.fillStyle = 'rgba(0,0,0,' + (lvl / 11) + ')';
		for(var me = 0; me < charLevels[lvl].length; me++) {
			c.fillRect(charLevels[lvl][me][0] * sizeX, charLevels[lvl][me][1] * sizeY, sizeX, sizeY);
		}
 	}

 	// Draw fires. Fires have 2 colors based on their number (2 or 3), with
 	// a random opacity set each draw to provide a flicker effect.
	for(var lvl = 1; lvl < fireLevels.length; lvl++) {
		c.fillStyle = 'rgba(' + '0 0 249,138,20 227,53,0'.split(' ')[lvl] + ',' + rand(7, 10) / 10 + ')';
		for(var me = 0; me < fireLevels[lvl].length; me++) {
			c.fillRect(fireLevels[lvl][me][0] * sizeX, fireLevels[lvl][me][1] * sizeY, sizeX, sizeY);
		}
 	}

 	// Draw embers. Embers always have the same color and opacity for simplicity's sake.
	for(var lvl = 1; lvl < emberLevels.length; lvl++) {
		c.fillStyle = 'rgba(' + '0 224,26,14,.8 249,58,26,.6 252,100,51,.4 252,138,70,.2'.split(' ')[lvl] +  ')';
		for(var me = 0; me < emberLevels[lvl].length; me++) {
			c.fillRect(emberLevels[lvl][me][0] * sizeX, emberLevels[lvl][me][1] * sizeY, sizeX, sizeY);
		}
 	}

},

// Set our grid size.
// There are always 80 rows and 80 columns, for 6400 total cells.
// We use Math.ceil to ensure there's no sub-pixel rendering or weird gaps.
setScale = function() {
	sizeX = Math.ceil(a.width/squares);
	sizeY = Math.ceil(a.height/squares);
},

// Determines the number of cells near the current cell that are on fire.
// This is more complex then the standard Game of Life stuff, because our fire
// array isn't just 1 or 0, it's -5 through 3.
// We're also looping around the edges of the array in our check, which causes
// the flames to do the same. This provides longer lasting, more interesting fires.
nearFire = function(w, x, y) {

	return 	isFire(w[(y == 0) ? squares - 1 : y - 1][(x == 0) ? squares - 1 : x - 1]) + 
			isFire(w[(y == 0) ? squares - 1 : y - 1][x]) + 
			isFire(w[(y == 0) ? squares - 1 : y - 1][(x == squares - 1) ? 0 : x + 1]) +
			isFire(w[y][(x == 0) ? squares - 1 : x - 1]) + 
			isFire(w[y][(x == squares - 1) ? 0 : x + 1]) + 
			isFire(w[(y == squares - 1) ? 0 : y + 1][(x == 0) ? squares - 1 : x - 1]) + 
			isFire(w[(y == squares - 1) ? 0 : y + 1][x]) + 
			isFire(w[(y == squares - 1) ? 0 : y + 1][(x == squares - 1) ? 0 : x + 1]);


	function isFire(w) {
		return w > 1 ? 1 : 0;
	}

},

// Create an empty array of arrays (of arrays).
generateEmpty = function(y, x) {
	var w = [];
	for(var r = 0; r < y; r++) {
		w[r] = [];
		for (var c = 0; c < x; c++) {
			w[r][c] = 0;
		}
	}
	return w;
},

// Seed the first frame with some random fire to get us started.
seed = function() { 
	fire = generateEmpty(squares, squares);
	eachCell(function(y, x) {
		fire[y][x] = (rand(0,1) <= .15) ? 2 : 0;
	});
},

// Generate a random landscape for our background. Save it as an image.
// Using hex here because we don't need opacity and it saves space
generateLandscape = function() {
	eachCell(function(y, x) {
		c.fillStyle = '#' + '3b2 5d3 3b3 1a2'.split(' ')[~~rand(0,4)];
		c.fillRect( x * sizeX, y * sizeY, sizeX, sizeY);
	});
	landscape = new Image();
	landscape.src = a.toDataURL();
},

// Run the passed function once per cell.
eachCell = function(w) {
	for (var y = 0; y < squares; y++) {
		for (var x = 0; x < squares; x++) {
			w(y, x);
		}
	}
},

// Generate a random number.
rand = function(x, y) {
	return x + Math.random() * (y - x);
}

init();