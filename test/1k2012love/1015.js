// ### Pixel Love - a js1k 2012 entry by @aaronsnoswell
// This demo is on [github](https://github.com/aaronsnoswell/js1k-2012-love)

// You can also browse the annotated source code at [http://aaronsnoswell.github.com/js1k-2012-love/docs/](http://aaronsnoswell.github.com/js1k-2012-love/docs/)

// Define some variables:

// A heart
var h = "♥",
	// The granularity - determines the size of each 'pixel'
	g = 5,
	// The threshold / limit, used for determining where to place pixels
	l = 10,
	// Arrays of locations for the outline and fill pixles
	outline = [],
	fill = [],
	// Where to draw the heart
	sx = 100,
	sy = 350,
	// The bounding box of the heart (computed later)
	box = [999, 999, 0, 0];

// Set the canvas size
c.width = 500;
c.height = 400;

// A utility function, used for looping over an x and y variable
var loopxy = function(w, h, step, callback) {
	for(var x=0; x<w; x+=step) {
		for(var y=0; y<h; y+=step) {
			callback(x, y, step);
		}
	}
}

// Shortcut method for clearing the canvas
a.clear = function() {
	a.clearRect(0, 0, c.width, c.height);
}

// Normalise browser font rendering in the canvas.
// See [https://twitter.com/#!/aaronsnoswell/status/165642474109419520](https://twitter.com/#!/aaronsnoswell/status/165642474109419520)
a.textBaseline = "bottom";
a.font="300pt arial";
a.lineWidth = g*2;

// Draw a filled heart on the canvas
a.fillText(h, sx, sy);

// Loop over the entire canvas and wherever there are filled pixels,
// store that location in the fill array
var d = a.getImageData(0, 0, c.width, c.height)
loopxy(d.width, d.height, g, function(x, y) {
	if(d.data[(x + y*d.width)*4+3] >= l) fill.push([x, y]);
});

// Clear the screen
a.clear();

// Draw an outline of a heart on the canvas
a.strokeText(h, sx, sy);

// Loop over the entire canvas once again. Wherever there are filled
// pixels, store that location in the outline array
var d = a.getImageData(0, 0, c.width, c.height)
loopxy(d.width, d.height, g, function(x, y) {
	if(d.data[(x + y*d.width)*4+3] >= l) {
		outline.push([x, y]);
		
		// Compute the bounding box of the heart
		if(x<box[0]) box[0] = x;
		if(x>box[2]) box[2] = x;
		if(y<box[1]) box[1] = y;
		if(y>box[3]) box[3] = y;
	}
});

// Find the middle x and y locations of the heart
box.push((box[0]+box[2])/2);
box.push((box[1]+box[3])/2);

// Store the current time as ms since epoch
var t0 = new Date().getTime();

// Use a recursive named function + setTimeout to ensure each frame has
// time to finish before the next one is drawn
(function render() {
	window.setTimeout(function() {
		// Time delta and elapsed time
		var d = new Date().getTime()-t0;
		t0 += d;
		
		// Clear the screen
		a.clear();
		
		// Draw the red heart
		a.fillStyle = "#f00";
		loopxy(fill.length, 1, 1, function(i) {
			var x = fill[i][0],
				y = fill[i][1];
			// Offset each pixel by a sinusoidal, time based ammount
			a.fillRect(x-g/2+Math.sin(t0/500+y/80)*(x-box[4])+10, y-g/2+10, g, g);
		})
		
		// Draw the outline using the same logic as above
		a.fillStyle = "#000";
		loopxy(outline.length, 1, 1, function(i) {
			var x = outline[i][0],
				y = outline[i][1];
			a.fillRect(x-g/2+Math.sin(t0/500+y/80)*(x-box[4])+10, y-g/2+10, g, g);
		})
		
		// Loop at 30fps
		render();
	}, 1/30);
})();