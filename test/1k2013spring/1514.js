// hideoous, embarassing spaghetti code just up ahead!
// you have been warned!
var	x, y,
	temp1,	// I don't even know which one of these does what anymore
	temp2,	// good luck trying to figure it out
	temp3,
	sqrs,	// cached number of tiles on the board
	strs,	// cached numbers of base 36-encoded strings needed to represent board
		// aka number of tiles / 25
	pic, guess, mark,
	n = 32,	// width and height of tiles
	d = 10, // default sized game board
	tf = 25,// just caching another common value
	stringify = JSON.stringify,
	L = top.location,
	mode = L.hash ? 1 : 2;	// mode 0 is creation, mode 1 is playing preset,
				// mode 2 is playing random, but i'll probably remove that
// required or GWT will trash the HTML shim's b object.
// can be removed after minification
b.dummy = [];

// comment this out and add it back in later, or GWT will inline it
function s(e){a.fillStyle=e}
function init() {
	pic = [], guess = [], mark = [];
	strs = (temp1 = L.hash.slice(1).split(',')).length;
	if (L.hash && mode == 1) {
		switch (strs) {
		case 1: d = 5; break;
		case 4: d = 10; break;
		case 9: d = 15; break;
		}
	}
	c.width = c.height = d*50, sqrs = d*d;
	// yes, this is on purpose
	a.font = "16pt m";
	// extract puzzle from string
	if (mode != 2) {
		for (y = 0; y < strs; y++) {
			temp2 = parseInt(temp1[y], 36);
			for (x = tf; x--;) {
				pic[y*tf+x] = 0;
				if (temp2 & 1 << x) {
					pic[y*tf+x] = 1;
				} 
			}
		}
	}
	for (y = d; y--;) {
		temp2 = temp3 = 0;
		for (x = 0; x < d; x++) {
			temp1 = y*d+x
			a.strokeRect(x * n, y * n, n, n);
			mark[temp1] = guess[temp1] = 0;
			// fills in numbers for each row... but not in edit mode, of course
			if (mode) {
				// only random for random mode
				if (mode == 2) {
					pic[temp1] = 0|Math.random()+.5;
				}
				if (pic[temp1]) temp2++;
				if (!pic[temp1] || x == d-1) {
					if (temp2) {
						a.fillText(temp2, d * 33 + temp3, y * n + 22);
						temp3 += n;
						temp2 = 0;
					}
				}
			}
		}
	}
	// fills in numbers for each column
	if (mode) {
		for (x = d; x--;) {
			temp2 = temp3 = 0;
			for (y = 0; y < d; y++) {
				temp1 = y*d+x
				if (pic[temp1]) temp2++;
				if (!pic[temp1] || y == d-1) {
					if (temp2) {
						a.fillText(temp2, x * n + 9, d * 35 + temp3);
						temp3 += n;
						temp2 = 0;
					}
				}
			}
		}
	}
}
c.onclick = function(e) {
	x = 0|e.pageX / n, y = 0|e.pageY / n, temp1 = y*d+x;
	if (x < d && y < d) {
		if (e.shiftKey && mode) {
			if (mark[temp1]--) {
				s('#fff');
			} else {
				guess[temp1] = 0;
				mark[temp1] = 1;
				s('#aaa');
			}
		} else if (guess[temp1]--) {
			s('#fff');
		} else {
			guess[temp1] = 1;
			mark[temp1] = 0;
			s('#000');
		}
		a.fillRect(x * n + 1, y * n + 1, 30, 30);
		if (mode) {
			if (stringify(pic) == stringify(guess)) {
				alert("YOUR WINNER");
			}
		} else {
			// encodes puzzles in URL like so:
			// for every 25 tiles, create a bitmask
			// then base 36-encode it
			for (y = 0, strs = d*d/tf, temp1=''; y < strs; y++) {
				for (x = tf, temp2 = 0; x--;) {
					if (guess[y * tf + x]) {
						temp2 |= 1 << x
					}
				}
				temp1 += temp2.toString(36);
				if (y<strs-1) {
					temp1 += ',';
				}
			}
			L.hash = temp1
		}
	}
}
init();
onkeydown = function(e) {
	temp1 = e.keyCode;
	// if keys 1, 2, or 3 are pressed, change board size
	if (temp1 > 48 && temp1 < 52) {
		d = 5 * (temp1 - 48);
		init();
	// if keys A, B, or C are pressed, change mode
	} else if (temp1 > 64 && temp1 < 68) {
		mode = temp1 - 65
		init();
	}
}