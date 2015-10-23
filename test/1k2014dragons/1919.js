//     _________________________________________________________________
//    /    __   __   __   __           __        __   __        __     /
//   /    /_/  /_/  /_/  /_/  /   /   /_  /     __/  / /  /_/  /_/    /
//  /    /    / /  / \  / /  /_  /_  /_  /_    /_   /_/    /  /_/    /
// /________________________________________________________________/
// |                                                                |
// | This is a clone of the popular game 2048 and your goal is to   |
// | merge tiles with the same number to eventually create a 2048   |
// | tile. In contrast to the original you play on two boards       |
// | simultaneously - the game ends if you create a 2048 tile or    |
// | there are no more possible moves on either board.              |
// |________________________________________________________________|
// |                                                                |
// | Controls                                                       |
// |                                                                |
// | * Use the arrow keys or swipe to move the tiles.               |
// | * Press space or tap anywhere to start a new game.             |
// |________________________________________________________________|
// |                                                                |
// | Some Programming Details                                       |
// |                                                                |
// | Compression:                                                   |
// |                                                                |
// |  Done with RegPack (http://siorki.github.io/regPack.html)      |
// |  by @Siorki. To make it more effective only characters that    |
// |  occur in strings and API calls have been used for variable    |
// |  names.                                                        |
// |                                                                |
// | Character Usage:                                               |
// |                                                                |
// |  in program: A   C D     G   I       M         R S T           |
// |              a b c d e f g h i   k l m n o p   r s t u v w x y |
// |  variables:  A   C                   M           S T           |
// |              a b c d e f g h i   k   m         r s         x y |
// |                                                                |
// | Board Layout:                                                  |
// |                                                                |
// |  layer | # | first  | second |                                 |
// |  ------+---+--------+--------+                                 |
// |  tiles | 0 |    0   |   16   |                                 |
// |  anim x| 1 |   32   |   48   |                                 |
// |  anim y| 2 |   64   |   80   |                                 |
// |  anim z| 3 |   96   |  112   |                                 |
// |                                                                |
// | Some Optimizations Used:                                       |
// |                                                                |
// |  * Used same variables for similar task to improve compression |
// |  * Used one algorithm for iterating over the board by mapping  |
// |    coordinates differently depending on the current view       |
// |  * Read touches in ontouchmove instead of changedTouches in    |
// |    ontouchend                                                  |
// |  * Chose scale to avoid setting font size                      |
// |  * All functions have the same signature for compression       |
// |________________________________________________________________|

// setup board and scale graphics
board = [c.scale(e = a.width / 400, e)],
// make sure that game gets initialized
key = stop = -5,

// Draw a rectangle or text.
// Params:
//  e - color
//  f - x coordinate
//  g - y coordinate
//  h - width of rectangle or text
//  i - height of rectangle or 0 to draw text
draw = function(e, f, g, h, i) {
	c.textAlign = 'center',
	c.fillStyle = e,
	f += 58 + k * 148,
	i ? c.fillRect(f - h / 2, g, h, i)
	  : c.fillText(h, f, g)
},

// Add up to two 2 or 4 tiles to random free spots.
// Params: none
addTile = function(e, f, g, h, i) {
	for(k = 2; k--;)
		for(i = 16 | Math.random() * 16; i-- && !(
			!board[map(0, i & 15, 2)] && (
				// start animation
				board[map(0, i & 15, 2, 3)] = 16,
				// set number
				board[map(0, i & 15, 2)] = 0 | Math.random() + 1.1
			)
		););
},

// Map view coordinates to board and select board and layer.
// Precondition: set k to select the board (0/1)
// Params:
//  e - x coordinate
//  f - y coordinate
//  g - direction (0..3)
//  h - layer
map = function(e, f, g, h, i) {
	// invert x coordinate
	g < 2 && (e = 3 - e);
	// swap x and y
	g & 1 && (i = e, e = f, f = i);
	// combine coordinates, board, and layer information
	return e * 4 | f + k * 16 | h * 32
},

// Set up controls
onkeydown = function(e, f, g, h, i) { key = e.keyCode - 37 },
ontouchmove = function(e, f, g, h, i) { e.preventDefault(); r = e.touches[0] },
ontouchstart = function(e, f, g, h, i) { p = e.touches[0] },
ontouchend = function(e, f, g, h, i) {
	// map swipe directions to key directions
	key = r ? (x = r.pageX - p.pageX) * x > (y = r.pageY - p.pageY) * y
				? x > 0 ? 2 : 0
				: y > 0 ? 3 : 1
			: -5,
	r = 0
},

setInterval(e = function(e, f, g, h, i) {
	// (re-)start
	if(key == -5) {
		for(i = 128; i--;)
			r = score = stop = lastAction = board[i] = 0;
		addTile();
		addTile();
	}
	// movement
	if(h = 0, !(key >> 2) && !stop) {
		// remember last action for animations
		lastAction = 2 + key & 3;
		// for each board
		for(k = 2; k--;)
			// for each row
			for(y = 4; y--;)
				// for each destination column
				for(x = 4; x--;)
					// for each source column
					for(i = x; i-- &&
						// skip to next destination if source and destination have different numbers
						!(
							board[map(x, y, key)] && board[map(i, y, key)] &&
							board[map(x, y, key)] - board[map(i, y, key)] || (
								board[map(x, y, key)]
									// merge tiles 
									? board[map(x, y, key)] == board[map(i, y, key)] && (
										// start "bump" animation
										board[map(x, y, key, 3)] = -4,
										// increase destination tile (values are stored as x instead of 2^x)
										// and score by value of merged tiles
										score += 1 << ++board[map(x, y, key)]
									)
									// move tile to empty destination
									: board[map(i, y, key)] && (
										// move tile
										board[map(x, y, key)] = board[map(i, y, key)],
										// start movement animation and check destination again because
										// the moved tile could be merged
										board[map(x, y, key, 1 + key % 2)] = (key<2 ? 32 : -32) * (x++ - i)
									)
							) && (
								// remove moved tile
								board[map(i, y, key)] = 0,
								// remember that we have moved a tile
								h = 1
							)
						);
					);
	}
	// check whether the game is over
	if(h) {
		// add random numbers
		addTile();
		// check if any move possible
		for(k = 2; k--;) {
			// for up/down and left/right orientation
			for(h = 0, key = 2; key--;)
				// for each row
				for(y = 4; y--;)
					// for each destination column
					for(x = 4; x--;)
						// we won when a 2048 is reached
						stop |= board[map(x, y, key)] > 10,
						// otherwise a move is possible if one square is empty
						h |= !board[map(x, y, key)] ||
							// ... or the tiles match
							x && board[map(x, y, key)] == board[map(x - 1, y, key)];
			// check for 2048 tile
			stop |= !h
		}
	}
	// draw each board
	for(k = 2; k--;) {
		// UI unrelated to a board
		if(k)
			// background
			draw('#fed', 0, 0, 1616, 1616),
			// score box
			draw('#ba9', 110, 5, 48, 16),
			draw('#fed', 110, 16, score),
			draw('#432', 40, 16, stop ? 'Game over' : '║2048');
		// board background
		draw('#ba9', 68, 28, 132, 132);
		// board
		for(y = 4; y--;)
			for(x = 4; x--;) {
				// abuse key and g variables for better compression
				g = key = lastAction,
				// map view to avoid drawing still tiles over moving ones
				e = x,
				f = y,
				g < 2 && (e = 3 - e);
				g & 1 && (i = e, e = f, f = i);
				// empty square background
				draw('#cba', e = e * 32 + 20, f = f * 32 + 32, 28, 28);
				// advance movement animation
				for(i = 2; i--;)
					board[map(x, y, key, i + 1)] += board[map(x, y, key, i + 1)] &&
						(board[map(x, y, key, i + 1)] < 0 ? 4 : -4);
				// advance "bump" and tile creation animations
				board[map(x, y, key, 3)] *= .7;
				if(board[map(x, y, key)])
					// draw tile background
					draw('rgb(' + [250, 250 - board[map(x, y, key)] * 15, 250 - board[map(x, y, key)] * 18] + ')',
						e + board[map(x, y, key, 1)],
						f + board[map(x, y, key, 2)] + board[map(x, y, key, 3)],
						i = 28 - board[map(x, y, key, 3)] * 2, i),
					// draw tile value
					draw('#432',
						e + board[map(x, y, key, 1)],
						f + board[map(x, y, key, 2)] + 18,
						1 << board[map(x, y, key)])
			}
	}
	key = k
}, 20)