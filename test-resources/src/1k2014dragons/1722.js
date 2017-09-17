var S = 70, x = S, y = S,
	sc = 0, // sc = score
	l = 3, // l = lives
	dx = 0, dy = 0,
	t = [], // t = tiles
	iG = true,
	now, delta, then = Date.now(),
	f = "fillStyle",
	fr = "fillRect",
	bp = "beginPath",
	k = "keyCode",
	o = c.fillText.bind(c),
	mr = Math.random,
	t1,
    i, j, z = 2*Math.PI, sp = '   '

c.font = "30px Arial"

b.onkeydown = function(e) {
    dx = (e[k] - 38) % 2, dy = (e[k] - 39) % 2;
	x = x + (dx * S)
	y = y + (dy * S)
	// left and top
	if (x < S) x = S
	if (y < S) y = S
	// right and bottom
	if (x > S * 9) x = S*9
	if (y > S * 8) y = S*8
	// check if leaf under player
	if (x > S) {
		if (!t[y/S-1][x/S-2]) {
			l--;
			x = y = S
		}
	}
	if (x/S-2 == px && y/S-1 == py) {
		sc++;
		dot()
	}
}

function dot() {
	px = rnd(0, 7), py = rnd(0, 7)
	if (t[py][px] == 0) t[py][px] = 3
}

function rnd(min, max) {
	return ~~(mr() * (max - min + 1)) + min
}

function init() {
	for (i=8;i--;) {
		t[i] = []
		for (j=8;j--;) {
			t[i][j] = rnd(0, 5)
		}
	}
	dot()
}

init();
(g = function() {
	// clear()
	c[f] = "#34495e";
	c[fr](0, 0, a.width, a.height)
	c[f] = "#2980b9"
	c[fr](90, 0, 610, 630)
	c.fill()

	now = Date.now()
	delta = now - then

    c[f] = "#27ae60"
	for (i=8;i--;) {
		for (j=8;j--;) {
			if (delta > 3000) {
				t[i][j] += rnd(-1, 1)
				if (t[i][j] < 0) t[i][j] = 0
				if (t[i][j] > 5) t[i][j] = 5
				dot()
				then = now
			}
            c[bp]()
			c.arc(S+S*(j+1), S*(i+1), 6*t[i][j], 0, z, true)
            c.fill()
		}
	}

	c[f] = "#e74c3c"
	c[bp]()
	c.arc(S+S*(px+1), S*(py+1), 6, 0, z, true)
	c.fill()
	//drawPlayer()
	c[f] = "#f1c40f"
	c[bp]()
	dx = dy = 0
	o("\u263b", x-16, y+8)

	//drawScores()
	if (l) o("Score "+sc+sp+"Lives "+l, 90, 660)
	else {
		iG = false;
		o("Game over", 770, 30)
	}
	if (iG) requestAnimationFrame(g)
})()