// Global variables
// z: tracker to block multiple clicks
// k: stroke counter
// x: current x position
// y: current y position
var z=k=0,x=y=70,boundingBoxArray=[];

// bounding box for hole ([0]) and main bounding walls
// [4] == 0: flip x
// [4] == 1: flip y
boundingBoxArray.push([740,540,750,550],[0,0,900,20,1],[0,0,20,700,0],[0,680,900,700,1],[880,0,900,700,0]);

// add 0-4 random obstacles to map
if (Math.round(Math.random())) {
	boundingBoxArray.push([0,130,128,150,1],[110,52,130,148,0],[112,50,128,70,1]);
}
if (Math.round(Math.random())) {
	boundingBoxArray.push([612,490,900,510,1],[610,492,630,638,0],[612,620,628,640,1]);
}
if (Math.round(Math.random())) {
	boundingBoxArray.push([110,332,130,638,0],[610,52,630,348,0],[112,330,628,350,1],[112,620,128,640,1],[612,50,628,70,1]);
}
if (Math.round(Math.random())) {
	boundingBoxArray.push([510,402,530,700,0],[512,400,828,420,1],[810,402,830,418,0]);
}

// initial display size
c.width=900,c.height=700;

// listen on canvas for click event
c.addEventListener('click', function(e) {
	// block multiple simultaneous clicks from affecting animation
	if (z++) return;

	// increment golf stroke counter
	k++;

	// console.log(e)

	// get X/Y data from event in all browsers
	m = e.offsetX || e.layerX;
	n = e.offsetY || e.layerY;

	// set velocity based on click, arbitrarily scaled down
	xVel = (m - x) / 3;
	yVel = (n - y) / 3;

	// keep velocity from going too fast--max 20 in each direction
	if (Math.abs(xVel) > 20) t = 20 / Math.abs(xVel), xVel *= t, yVel *= t;
	if (Math.abs(yVel) > 20) t = 20 / Math.abs(yVel), xVel *= t, yVel *= t;

	// start animation
	setTimeout(tick, 30);
});

// one frame of movement
function tick () {

	// get new position of ball
	x += xVel;
	y += yVel;

	// run twice to check both x and y collisions
	for (j = 0; ++j < 3;) {

		// check all bounding boxes
		for (s = i = 0, r = 1; ++i < boundingBoxArray.length;) {
			if (x > boundingBoxArray[i][0] && x < boundingBoxArray[i][2] && y > boundingBoxArray[i][1] && y < boundingBoxArray[i][3]) {
				// set data based on bounce direction
				boundingBoxArray[i][4] ? (m = y, n = yVel) : (m = x, n = xVel);

				// check to see which bounding line was crossed first
				if (Math.abs(q = (boundingBoxArray[i][boundingBoxArray[i][4]]-m+n)/n) < Math.abs(r)) r=q,s = boundingBoxArray[i];
				if (Math.abs(q = (boundingBoxArray[i][boundingBoxArray[i][4] + 2]-m+n)/n) < Math.abs(r)) r=q,s = boundingBoxArray[i];
			}
		}

		// bounce if a bounding line was crossed
		if (s) s[4] ? (y=(y-yVel<s[1])?s[1]:s[3],yVel=-yVel) : (x=(x-xVel<s[0])?s[0]:s[2],xVel=-xVel);

	}

	// reduce velocity by coefficient of friction
	xVel *= .9;
	yVel *= .9;

	// show new frame
	drawMap();

	// check for success
	i = 0;
	if (x > boundingBoxArray[i][0] && x < boundingBoxArray[i][2] && y > boundingBoxArray[i][1] && y < boundingBoxArray[i][3]) { // a winner is you!

		// draw flag
		a.fillStyle='#f00';
		a.fillRect(744,464,35,20);

		// draw flagpole
		a.fillStyle='#000';
		a.fillRect(744,464,2,80);

		// print score on flag
		a.fillText(k, 750, 480);

		return; // clicks still disabled, game over
	}

	// check velocity, keep looping if the ball is still moving
	if (xVel * xVel + yVel * yVel > 1) { // square both velocities to strip signs
		setTimeout(tick, 30);
		return;
	}

	// remove blocking variable to allow next click
	z = 0;
}

function drawMap () {
	// refresh canvas
	c.width = c.width;

	// putting surface
	a.fillStyle='#0c0';
	a.fillRect(0,0,900,700);

	// all bounding boxes
	a.fillStyle='#444';
	for (i = 0; ++i < boundingBoxArray.length;) a.fillRect(boundingBoxArray[i][0],boundingBoxArray[i][1],boundingBoxArray[i][2]-boundingBoxArray[i][0],boundingBoxArray[i][3]-boundingBoxArray[i][1]);

	// golf hole
	a.fillRect(740,542,10,6);
	a.fillRect(742,540,6,10);

	// golf ball
	a.fillStyle='#fff';
	a.fillRect(x-1,y-2,2,4);
	a.fillRect(x-2,y-1,4,2);
}

// initial render
drawMap();