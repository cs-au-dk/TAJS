/*
 * Props for the idea on how to draw a heart go to: http://www.mathematische-basteleien.de/heart.htm
 * I also had a working version using single pixels and a function (see below), but drawing arcs and lines is WAY faster.
 * The same goes for drawing text, which is WAY to slow.
 *
 * Original heart function
 * var x = x/s/.02 - (h.x*50/s);
 * var y = -y/s/.02 + (h.y*50.5/s);
 * (x*x + 2 * (b = (y - .9 * Math.sqrt( x<0?-x:x ))) * b) && (...draw red pixel...)
 *
 *
 * Each heart is stored inside an array: [size, x, y, color, time added]
 */

var
	//Set width and height to window size
	//We use the half of width and height quite some times
	//p = paused or not (truthy)
	j = (c.width = 998)/2,
	p = k = (c.height = 400)/2,

	//The array containing the hearts
	h = [],

	//t = time, d = direction of player (-1,0,+1), x = player position
	t = d = x = 0,

	//will reference the size of the current heart inside the loop
	g,

	M = Math,
	P = M.PI,
	C = M.cos;


//Give me that cinema feeling
with(c.style){display='block';margin='auto';border='solid #000';borderWidth='99px 2px';background='#000'}


//Now we use b and c as usual variables, because we won't need body or canvas


onkeydown = function(e) {
	e = e.which;

	//'p' === 80
	p = e-80;

	if(!d) {
		//left === 37
		e-37 || (d = -1);

		//right === 39
		e-39 || (d = 1)

	}
}

onkeyup = function() {d = 0}


setInterval(function(i) {
	//Now scope everything to the canvas context, because we are doing a shitload of method calls
	if(p) with(a) {
		save(
			//We only need clearRect for the first seconds when the hearts don't fill up the whole space
			clearRect(i = 0, 0, j*2, k*2)
		);

		//Iterate over all hearts
		//The translation will keep it centered around the 8th element
		for(translate(-C((h[8] && h[8][4] ||  t)/100) * j/2 + j, 0); b = h[i]; i++) {
		//for(save(); b = h[i]; i++) {

			//Make the heart bigger and keep track of the new size, because we need this value often
			//This value will also come in handy after the loop is finished, because we need the size of the last heart for removing it
			g = b[0] *= 1.1;

			/*
			 * blame @jedschmidt and @140bytes for this looking so fucked up :-D
			 * we are basically saving semicolons by nesting function calls (only makes sense if function doesn't expect ANY params).
			 */

			//Start a new path for our heart
			beginPath(
				//Some decent black thin line
				fillStyle = b[3]
			);

			//Left arc/curve of the heart
			arc(b[1] - g, c = b[2] - g, g, P * .8, P * 2);

			//Right arc/curve of the heart
			arc(b[1] + g, c, g, P, P * 2.2)

			//At the end, fill the heart
			fill(
				//The left part (close the heart). Only needed because we are drawing a stroke! Wouldn't for just fills.
				closePath(
					//The right lower part of the "peak"
					lineTo(
						//Parameters for the line segment (x, y + size*2)
						b[1], b[2] + g * 2
					)
				)
			);

			//We use the 38th heart for clipping. It's no rocket science, but looks OK.
			i - 38 || clip(save())
		}

		//Only restore if we clipped
		i>38 && restore();

		//Increment the time value and compute sine for new hearts position
		c = C(t++/100) * j/2;

		//Calculate the color (used for hearts and for arrow), based on sine and some random spice
		b = (210 * (b = C(t/15)) * b * b * b + 24 + M.random()*30) | 0;

		//(!i || g > 1) && (h[i] = [1, c, k, 'rgb(' + b + ',0,0)', t]);

		restore(
			//Append a new heart
			h[i] = [1, c, k, 'rgb(' + b + ',0,0)', t]
		);

		//Arrow will be gray
		fillStyle = 'rgb(' + b + ',' + b + ',' + b + ')';

		save(
			//Players position
			c = -c*2 + j*2 - 50 + x
		);

		scale(1, .5);

		save(
			//cursive font family gives us some perspective look
			font = '150px cursive'
		);


		//Rotate the arrow when moving left or right
		translate(j, k+50);
		rotate(.05*d);
		translate(-j, -k-50);

		restore(
			//Draw the player
			fillText('↑', c, k*2 + 150)
		);

		//Render the score
		fillText(t>=1024 ? '>1k' : t, 10, 150);

		//Remove the first element if big enough
		h[0][0] > j && h.shift();

		restore(
			//Modify player offset depending on direction
			x += d*6
		);

		//Reset game when hitting wall
		if(M.abs(c - j + 50) > 200) {
			alert('DOH! You hit the wall.\nYour score: ' + t);
			d = t = x = 0;
			h = [];
		}
	}
}, 33);