// I used YUI Compressor for the first pass.

// That was followed by some manual tweaking to get the size down to 1178B.

// That was then run through the "First Crush" entry by Tim Down

// for a final size of 1023B.



// Use human readable names for a, b, c while developing.

// These are removed after compressing.



// Use shortenable names for Math and window.addEventListener

(function (body, canvas, context, Math, addEvent) {

	// variable declarations.

	// These help YUI compressor know what variables can be munged.

	// They will be removed after compressing.

	var step, cos, sin, round, abs, timeout, xMid, yMid, R, r, Rr, p, t, acanvas, acontext, height, width, nextX, nextY, style;



	// set up some shorthands

	step = Math.PI/720;

	cos = Math.cos;

	sin = Math.sin;

	round = Math.round;

	abs = Math.abs;

	

	// Initial drawing parameters

	R = r = p = 100;



	body.style.margin = timeout = 0;



	// copy the canvas, so we can have one persistent, and one animated.

	// this allows us to animate the circles while drawing without redrawing

	// the entire canvas on each frame

	acanvas = canvas.cloneNode(0);

	acontext = acanvas.getContext("2d");

	style = acanvas.style;

	style.position = "absolute";

	style.top = style.left = 0;

	body.appendChild(acanvas);



	function plot() {

		nextX = xMid + Rr*sin(t) + p*sin(Rr*t/r);

		nextY = yMid + Rr*cos(t) + p*cos(Rr*t/r);

	}



	// draw a segment of the line

	function draw (i, ratio1, ratio2) {



		// draw the next segment of the line

		context.beginPath();

		context.moveTo(nextX, nextY);



		for (i = 0; i++ < 5;){

			t += step;

			plot();

			context.lineTo(nextX, nextY);

		}



		stroke(context, "#f33");



		drawCircles();



		// animate the next line segment

		ratio2 = (ratio1 = t / Math.PI / 2) * R / r;

		if (abs(ratio1 - round(ratio1)) + abs(ratio2 - round(ratio2)) > 0.0001)

			timeout = setTimeout(draw, 32);

	}



	function drawCircles() {

		clear(acontext);

		circle(xMid, yMid, R);

		circle(xMid + Rr*sin(t), yMid + Rr*cos(t), abs(r));

		circle(nextX, nextY, 3);

	}

	function circle(x, y, r) {

		acontext.beginPath();

		acontext.arc(x, y, r, 0, 7);

		stroke(acontext, "#ccc");

	}

	function label(text, y) {

		context.font = '14px sans-serif';

		context.fillText(text, 9, y);

	}



	function stroke(context, color) {

		context.lineWidth = 1;

		context.strokeStyle = color;

		context.stroke();

	}



	function clear(context) {

		context.clearRect(0, 0, width, height);

	}



	// Reset the canvas when the window is resized, or the drawing parameters change

	function initCanvas() {



		clearTimeout(timeout);

		timeout = t = 0;



		Rr = R+r;



		height = canvas.height = acanvas.height = document.documentElement.clientHeight - 9;

		width = canvas.width = acanvas.width = body.clientWidth;



		xMid = round(width/2);

		yMid = round(height/2);



		plot();



		drawCircles();



		clear(context);

		// Some labels here to make the experience a bit more user friendly.

		label("Arrow/Home/End keys adjust. Space draws.", 32);

		label("R1: "+R +", R2: "+r+", pen: "+p, 50);

		label("\u00a92012 Andrew Cattau", height - 5);

	}



	function keypress(evt, keyCode) {

		keyCode = evt.keyCode - 32;



		// if timeout is set, we've already started drawing,

		// so don't draw again...

		keyCode == 0 && !timeout && draw();



		// Alternate syntax here is shorter than the more typical

		//  if (keyCode==37) R++;

		if (keyCode > 2 && keyCode < 9) {

			if (keyCode == 6) { r++; p++; } // up

			if (keyCode == 8) { r--; p--; } // down

			keyCode == 5 && R--; // left

			keyCode == 7 && R++; // right

			keyCode == 4 && p--; // home

			keyCode == 3 && p++; // end



			initCanvas();

		}

	

	}



	addEvent("resize", initCanvas, 0);

	addEvent("keydown", keypress, 1);



	initCanvas();

})(b, c, a, Math, window.addEventListener)