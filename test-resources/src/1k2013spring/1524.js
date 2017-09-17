// ===
// 		Daisy Chain!
//						===

// * unhappy daisies in Opera Browser due to rendering glitch

// properties
// var
//width, height,

// plot
pi = Math.PI;
//horizon,
//block,

// palette
//sky;

// body width, 3:1 ratio
c.width = width = b.clientWidth;
c.height = height = width / 3;

// set plot
horizon = height * 0.2;
block = 10;

// set sky drop
sky = a.createLinearGradient(0, 0, 0, height);
sky.addColorStop(0, '#0FF');
sky.addColorStop(1, '#00F');

// set fill to sky
a.fillStyle = sky;
a.fillRect(0, 0, width, height);

/**
 *	Generate random number.
 *	@param {Number} min Minimum number to generate
 *	@param {Number} max Maximum number to generate
 */
function random_(min, max) {
	// _.random()
	return min + Math.floor(Math.random() * (max - min + 1));
}

/**
 *	Intelligently render on framerate intervals. (reduces lag)
 *	@param {Function} frame Callback function to generate frame
 */
function render_(frame) {
	// requestAnimationFrame shim (@paul_irish)
	(window.requestAnimationFrame ||
		window.webkitRequestAnimationFrame ||
		window.mozRequestAnimationFrame ||
		function(fn){
			window.setTimeout(fn, 1000 / 60);
		})(frame);
}

/**
 *	Blossoms a daisy!
 */
function blossom() {
	// set scope
	var multiplier, posMultiplier,
		startX, startY,
		stopX, stopY,
		ctrlX, ctrlY,
		deltaX, deltaY,
		diffX, diffY,
		posX, posY;

	// set random multiplier, determines size
	posMultiplier = 0;
	multiplier = random_(5, (horizon * 3) / block);

	// set where to start
	posX = startX = random_(block, width - block);
	posY = startY = height - horizon + block;

	// set where to end
	stopX = random_(startX - multiplier, startX + multiplier);
	stopY = startY - (block * multiplier);

	// set gradient for quadratic curve
	ctrlX = random_(startX - multiplier, stopX + multiplier);
	ctrlY = random_(startY, stopY);

	// determine velocity in x and y directions
	deltaX = Math.abs(stopX - startX);
	deltaY = Math.abs(stopY - startY);

	// velocity, where diff = distance per frame
	diffX = diffY = 1; (deltaX > deltaY) ? diffY = deltaY / deltaX : diffX = deltaX / deltaY;

	/**
	 *	Renders daisy flower.
	 */
	function flower() {
		// render stamen
		a.beginPath();
		a.fillStyle = '#FF0';
		a.arc(stopX, stopY, (block * posMultiplier)/pi/1.5/1.5, 0, pi*2);
		a.fill();
		//a.restore();

		// render petals (unicode chars ftw)
		a.fillStyle = '#FFF';
		a.font = (block * posMultiplier) + "pt serif";
		a.fillText("\u273f", stopX - (block * posMultiplier)/2, stopY + (block * posMultiplier)/2, block * posMultiplier);

		// progress position
		posMultiplier++;

		// loop until flower is fully rendered
		if(posMultiplier !== multiplier) render_(flower);
	}

	/**
	 *	Renders daisy stalk.
	 */
	function stalk() {
		// render quadratic curve
		a.beginPath();
		a.lineWidth = block;
		a.strokeStyle = '#000';
		a.moveTo(startX, startY);
		a.quadraticCurveTo(ctrlX, ctrlY, posX, posY);
		a.stroke();
		//a.restore();

		// set ground drop (maintains position above stalk)
		a.fillStyle = '#080';
		a.fillRect(0, height - horizon, width, horizon);

		// progress position
		posX += diffX;
		posY -= diffY;

		// loop, or move on to the flower
		(posY !== stopY) ? render_(stalk) : flower();
	}

	// render only when coordinates are different, else try again
	// fixes rendering bug where flowers would appear without stalk
	(stopX !== startX) ? stalk() : blossom();

	// and we're done!
	// return;
}

// generate random number of daisies, *spor*adically between 0.5s and 8s
// excuse the pun...
blossom();
for(var x=0, limit=random_(0, 25); x<limit; x++) {
	window.setTimeout(blossom, random_(500, 8000));
}