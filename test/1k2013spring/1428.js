var s = c.width = c.height = 256; //Size
var m = s / 2; //Half size
var l = 64; //Small unit to position players

a.fillRect(0, 0, c.width, c.height); //Black background

var d, //Pixel data
	px = {}; //Collision data

var p = [ //Players
	new P(l, m, 1, 0, 0),
	new P(s - l, m, -1, 0, 1),
	new P(s - m, l, 0, 1, 2),
	new P(s - m, s - l, 0, -1, 3)
];

setInterval(function() {
	d = a.getImageData(0, 0, s, s); //Get pixels

	for(var i = 0; i < p.length; i++) {
		var v = p[i];
		v.u(d); //Update player

		if (i > 0) { //For NPCs
			if (Math.random() < 0.01 || v.a()) //If wall ahead or random chance
				if (Math.random() < 0.5)
					v.r(); //Turn right
				else
					v.l(); //Turn left
		}
	}

	a.putImageData(d, 0, 0); //Set pixels
}, 33);

function gp(x, y) { //Check for collision
	if (!px[x]) //Create 2nd dimmension if doesn't exist
		px[x] = []; //Creat if not
	if (y == 0 || y == s || x == 0 || x == s) //Exterior walls
		return 1;
	return px[x][y];
}

function P(x, y, sx, sy, c) { //Player
	var t = this;
	t.x = x; //x pos
	t.y = y; //y pos
	t.sx = sx; //x speed
	t.sy = sy; //y speed
	t.c = c; //color

	t.u = function(d) { //update
		if (!px[t.x]) //Create 2nd dimmension if doesn't exist
			px[t.x] = []; //Creat if not
		px[t.x][t.y] = 1; //Set collision

		c = [[0, 128, 255],[255, 0, 0],[0, 255, 0],[255, 255, 0]][t.c]; //Colors

		//Move
		t.x += t.sx;
		t.y += t.sy;

		//If hit something stop moving
		if (gp(t.x, t.y)) {
			t.sx = 0;
			t.sy = 0;
		}

		//Set pixel data
		if (t.sx + t.sy) {
			var i = (t.x + t.y * s) * 4;
			d.data[i] = c[0]; //red
			d.data[i + 1] = c[1]; //green
			d.data[i + 2] = c[2]; //blue
		}
	}

	//Turn right
	t.r = function() {
		nx = -t.sy;
		ny = t.sx;
		t.sx = nx;
		t.sy = ny;
	}

	//Turn left
	t.l = function() {
		nx = t.sy;
		ny = -t.sx;
		t.sx = nx;
		t.sy = ny;
	}

	//Check for collisions ahead
	t.a = function() {
		ax = t.x + t.sx * 8;
		ay = t.y + t.sy * 8;
		return gp(ax, ay);
	}
}

//Player controls
document.onkeydown = function(e) {
	if (e.keyCode == 39) p[0].r(); //Right key
	if (e.keyCode == 37) p[0].l(); //Left key
}