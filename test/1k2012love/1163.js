(function JS1K_love(ctx,canvas,body){
 
 
	var // Global variables and settings
		width  = canvas.width  = 600,
		height = canvas.height = 450,
		list   = ["♂","♀","#6371b7","#db7189"],
		particles = [],
		love_str,
		love_tick=-1,
		tick   = (new Date).getTime(),
		random = Math.random,
		floor  = Math.floor,
		round  = Math.round
	;
 
 
	// Canvas in the center, define the background color.
	body.style.background = "#724567";
	body.style.textAlign  = "center";
 
 
	// Function to draw a heart shape.
	function draw_heart() {
		ctx.lineWidth = 30;
		ctx.beginPath();
		ctx.moveTo( width/2, height/3);
		ctx.bezierCurveTo( width/10,       0,         0, height*0.6, width/2, height*0.9);
		ctx.bezierCurveTo( width, height*0.6, width*0.9,          0, width/2, height/3 );
		ctx.closePath();
		ctx.stroke();
	}
 
 
	// Return particles from canvas pixels.
	function get_particles_from_heart_pixels() {
		var pixels = ctx.getImageData( 0, 0, width, height).data;
		var p = [];
		// Put a particle with a chance of 1.5% where there is the heart shape.
		for ( var i=0; i<pixels.length; i+=4 )
			if ( pixels[i+3] && random()<.015 )
				p.push({ sex:round(random()), x:i/4%width, y:i/4/width }); // randomly select the particle sex.
		return p;
	}
 
 
	// Generate the animation
	function process() {
 
		// Generate settings
		var
			time     =  (new Date).getTime() - tick,
			radius   =  Math.cos(time*3E-4),
			gradient =  ctx.createRadialGradient( width/2, height/2, 0, width/2, height/2, width/2 ),
			i,
			particle,
			angle,
			_x,
			_y
		;
 
		// Generate the dradiant effect (move with time)
		gradient.addColorStop( 0, "#c68fb8" );
		gradient.addColorStop( 0.6-Math.sin(time*3E-4)/10, "#724567" );
 
		// Blur effect, just erase with 0.3 opacity
		ctx.globalAlpha = 0.3;
		ctx.fillStyle   = gradient;
		ctx.fillRect( 0, 0, width, height );
 
		// Draw particles
		for ( i in particles ) {
 
			particle = particles[i];
			angle     = Math.PI * 5 * Math.sin(time*3E-4) + i * Math.PI/30;
			_x        = Math.sin(angle);
			_y        = Math.cos(angle);
 
			// Change position
			particle.x += radius * _x;
			particle.y += radius * _y;
 
			// Change size and opacity and generate particle
			ctx.font  = floor( _x*5 + 17) + "px Arial";
			ctx.globalAlpha = 1.5 - ( _x + 1.5 ) / 2;
			ctx.strokeStyle = list[particle.sex + 2];
			ctx.strokeText( list[particle.sex], particle.x, particle.y );
		}
 
		// Restore opacity for the LOVE, and find size.
		ctx.globalAlpha = 1;
		ctx.font = "bold " + floor( _x * 2 + 55 ) + "px Arial";
		ctx.fillStyle   = "rgba(255,255,255,0.1)";
		ctx.strokeStyle = "rgba(255,255,255,0.2)";
		ctx.textAlign   = "center";
 
		// Change sex in LOVE, every 4secs.
		if ( floor(time/4000) !== love_tick ) {
			love_tick = floor(time/4000);
			love_str  = list[ round(random()) ] + " LOVE " + list[ round(random()) ];
		}
 
		// Draw Love text.
		ctx.fillText( love_str, width/2, height/2 + 30 );
		ctx.strokeText( love_str, width/2, height/2 + 30);
	}
 
 
	// start the contest
	(function init() {
		draw_heart(); // Draw the heart
		particles = get_particles_from_heart_pixels(); // Generate particles from heart pixels
		ctx.clearRect( 0, 0, width, height ); // clear the canvas
		ctx.lineWidth = 2; // restore the line width
		setInterval( process, 30 ); // animation.
	})()
 
})(a,c,b);