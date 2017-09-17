/**
 * BREATH
 * js1k interactive demo
 * subzey, 2014
**/

// Let canvas be positioned absolutely.
// Its position is already (0, 0), so we don't need to set left/top
a.style.position = 'absolute';

// Clone canvas element. It already have position: absolute
clonedCanvas = a.cloneNode();
// Append it to body. Now we have two layers for drawing
b.appendChild(clonedCanvas);
// Get its context
clonedCanvasContext = clonedCanvas.getContext('2d');

// Global flamethrowing state
fireDisabled = true;

// Counter for flashing left button
hintCounter = 0;

// Change context size while keeping canvas size
// Flame would be shown stretched
a.width = 150;
a.height = 60;

// Get image data. We don't need actual pixel data, it will be used for
// rendering
imageData = c.getImageData(0, 0, a.width, a.height);
// Get reference to array with channel RGBA data
pixelChannelValues = imageData.data;


// Initialize intensity array. It's empty, we can think of it as full of
// undefineds
intensityData = [];

// Every 40 ms...
setInterval(function() {
	// new values
	var newData = [];
	for (var i = 0; i < a.width * a.height; i++) {
		// x coordinate
		var x = i % a.width;
		// clonedCanvas coefficient. FLames should move down at the left side of the
		// screen and up at the right
		var yCoefficient = 1 + x / 60;
		// Intensity for pixel
		var rand = Math.random();
		var L;
		if (
			(i!= 3179 || rand > .2) && // small "idle" flame
			(x !== 29 || i < 16 * a.width || i > 22 * a.width || fireDisabled)
		) {
			// Normal pixels. Use matrix convolution
			L = (
				// x3 from self
				intensityData[i] * 3 +
				// x(1..3.5) from bottom pixel
				intensityData[i + a.width] * yCoefficient +
				// x(3.5..1) from top pixel
				intensityData[i - a.width] * (5 - yCoefficient) +
				// x(4.7..5) from left pixel. Flames should move slightly
				// slower closed to the right edge
				// Flame will wrap around the screen, that's okay
				intensityData[i - 1] * (5 - x / 500)
			) / (
				// Carefully picked number
				12.37 +
				// Randomness! This thing makes flame fuzzy
				rand
			) || 0;
		} else {
			// Row of "iginition" pixels. Located in mounth of dragon
			L = 255;
		}
		// Save
		newData[i] = L;
		// Get offset (R,G,B,A for each pixel)
		var offset = i * 4;
		// Red. Equals intensity
		pixelChannelValues[offset] = L;
		// Green. 0 for values 0..127, then goes up to 255 at 255
		// Flame becomes yellow at high intensity
		pixelChannelValues[offset + 1] = L * 2 - 255;
		// Blue. Just vertical gradient. Values are small compared to
		// R and G, so it have almost no effect on flame
		pixelChannelValues[offset + 2] = i / 150;
		// Transparency. Always opaque.
		pixelChannelValues[offset + 3] = 255;
	}
	// Replace the data. It would be used for calculation at next "tick"
	intensityData = newData;
	// Draw flames on canvas
	c.putImageData(imageData, 0, 0);

	// Okay, now it's time to draw dragon over the flames

	// Reset canvas
	clonedCanvas.width=clonedCanvas.width;
	// Set fill and stroke color depending on state
	clonedCanvasContext.fillStyle=fireDisabled?'#000':'#100';
	clonedCanvasContext.strokeStyle=fireDisabled?'#333':'#430';
	// Apply transform so we can draw in absolute coordinates
	clonedCanvasContext.scale(clonedCanvas.width/200,clonedCanvas.height/200);

	// Draw background wing, fill and stroke
	clonedCanvasContext.beginPath();
	clonedCanvasContext.moveTo(0,145);
	clonedCanvasContext.bezierCurveTo(25,70,30,60,39,300);
	clonedCanvasContext.fill();
	clonedCanvasContext.stroke();
	
	// Draw head end body
	clonedCanvasContext.beginPath();
	clonedCanvasContext.moveTo(52,56);
	clonedCanvasContext.bezierCurveTo(54,35,34,35,25,22);
	clonedCanvasContext.bezierCurveTo(25,23,-50,-50,0,240);
	clonedCanvasContext.bezierCurveTo(60,125,0,120,23,77);
	clonedCanvasContext.bezierCurveTo(49,91,36,60,36,55);
	
	// Draw eyebrow
	clonedCanvasContext.moveTo(26,44);
	clonedCanvasContext.bezierCurveTo(26,40,28,40,16,35);

	// Fill head, body and eyebrow. We don't actually need to fill eyebrow,
	// but nothing bad would happed if we do it.
	clonedCanvasContext.fill();
	
	// Draw foreground wing. We don't need to fill it
	clonedCanvasContext.moveTo(0,99);
	clonedCanvasContext.bezierCurveTo(0,99,20,70,10,300);

	// Stroke head, body, eyebrow and wing
	clonedCanvasContext.stroke();

	// Now we got huge mess near the mouth region, we should cover it will
	// filled region.
	
	// Draw mouth
	clonedCanvasContext.beginPath();
	clonedCanvasContext.moveTo(40,56);
	clonedCanvasContext.bezierCurveTo(52,116,-24,38,52,56);
	
	// Draw nostril
	clonedCanvasContext.moveTo(42,50);
	clonedCanvasContext.bezierCurveTo(38,47,38,46,37,49);
	
	if (!fireDisabled){
		// If dragon activated, change the color (of mouth and nostril)
		// to yellow
		clonedCanvasContext.fillStyle='#FF0';
	}

	// Fill mouth and nostril
	clonedCanvasContext.fill();
	if (fireDisabled){
		// Stroke if fill is black
		clonedCanvasContext.stroke();
	}

	// After 4 seconds and if hintCounter is not NaN (disabled)
	if(hintCounter++>99){
		// Draw most of the mouse icon
		clonedCanvasContext.beginPath();
		clonedCanvasContext.moveTo(170,124);
		clonedCanvasContext.bezierCurveTo(208,120,166,247,155,149);
		// Fill it (with black) and stroke
		clonedCanvasContext.fill();
		clonedCanvasContext.stroke();

		// Draw left button
		clonedCanvasContext.beginPath();
		clonedCanvasContext.moveTo(185,149);
		clonedCanvasContext.lineTo(155,149);
		clonedCanvasContext.bezierCurveTo(155,124,163,124,170,124);
		clonedCanvasContext.lineTo(170,149);
		if (hintCounter / 9 & 1){
			// Color depends on timer: flashing effect
			clonedCanvasContext.fillStyle=clonedCanvasContext.strokeStyle
		}
		
		// Fill and stroke left button
		clonedCanvasContext.fill();
		clonedCanvasContext.stroke()
	}
	// Restore fillStyle
	clonedCanvasContext.fillStyle=fireDisabled?'#000':'#100';

	// Draw and fill teeth
	clonedCanvasContext.beginPath();
	clonedCanvasContext.moveTo(34,79);
	clonedCanvasContext.bezierCurveTo(34,69,34,69,32,78);
	clonedCanvasContext.moveTo(39,76);
	clonedCanvasContext.bezierCurveTo(38,69,38,69,37,78);
	clonedCanvasContext.moveTo(35,54);
	clonedCanvasContext.bezierCurveTo(35,66,35,66,37,54);
	clonedCanvasContext.moveTo(43,55);
	clonedCanvasContext.bezierCurveTo(43,65,43,65,45,55);
	clonedCanvasContext.fill();

	// Draw and fill an eye
	clonedCanvasContext.fillStyle='#E70';
	clonedCanvasContext.beginPath();
	clonedCanvasContext.moveTo(24,39);
	clonedCanvasContext.bezierCurveTo(24,43,22,43,22,38);
	clonedCanvasContext.fill()
}, 40);

b.onmousedown = b.onmouseup = function() {
	// Toggle state
	fireDisabled = !fireDisabled;
	// Disable hint counter (comparison will always yield false for NaN)
	hintCounter = NaN;
};