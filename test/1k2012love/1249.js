/*
Use left and right arrows to move the trampoline and make her gather all the love.

Inspired from classic Atari game Clowns & Balloons.

Minified by jscompress.com, crushed by @tpdown's "First Crush".
*/
//  /(,")♥♥(".)
//   /♥\    /█\
//  _| |_  _| |_
			// Canvas
			W = c.width = 400;
			H = c.height = 300;
			
			// Sound effects
			s = new Audio("data:audio/wav;base64,UklGRqSIFQBXQVZFZm10IBAAAAABAAEARKwAAESsAAABAAgAZGF0YYCI00ff00ff");
			t = new Audio("data:audio/wav;base64,UklGRqSIFQBXQVZFZm10IBAAAAABAAEARKwAAESsAAABAAgAZGF0YYCIabcdedcb");
			s.play();
			t.play();
			
			// Ball
			x = 100; // horizontal position
			y = 20;  // vertical position
			v = 1.5; // horizontal velocity
			w = 0;   // vertical velocity
			r = 1/4; // vertical acceleration
			
			// Trampoline
			X = 150;    // horizontal position
			Y = H - 13; // vertical position
			D = 0;      // horizontal velocity
			S = 4;      // horizontal acceleration
			L = 60;     // width
			
			// Hearts
			n = 18;      // number of hearts
			h2 = W - (h1 = h3 = -500); // horizontal positions
			v1 = [];     // visibility array of ->
			v2 = [];     // visibility array of <-
			v3 = [];     // visibility array of ->
			
			// Event listeners
			window.addEventListener("keydown", function (e) {
				k = e.keyCode;
				D = (k == 39) * S - (k == 37) * S;
			});
			window.addEventListener("keyup", function ku(e){
				D = 0;
			});
			setInterval(function (e) {
				//c.width = c.width;
				a.fillStyle = "#234";
				a.fillRect(0, 0, W, H);
				
				// Draw the hearts.
				f = true;
				h1 = dh(h1, 40, v1, 1, 0, W, "#f90");  //"#889"
				h2 = dh(h2, 60, v2, -1, W, 0, "#c99");
				h3 = dh(h3, 80, v3, 1, 0, W, "#f50");
				if (f)
					return;

				// Draw the ball.
				a.font = "28px sans";
				a.fillStyle = "#ddb";
				a.fillText("유", x, y);
				
				// Draw the trampoline.
				a.font = "25px sans";
				a.fillText("웃", X - 21, Y + 7);
				a.fillText("웃", X + L - 4, Y + 7);
				a.fillStyle = "#a98";
				a.fillRect(X, Y, L, 2);
				
				// Move the trampoline.
				X += (X + D > 0 & X + D < W - L) ? D : 0;
				
				// Move the ball.
				x += v;
				y += w;
				
				// Hit test between the ball and the trampoline.
				if (y > Y) {
					if (x + 28 >= X & x <= X + L & S != 0) {
						// Hit!
						//v -= (Math.abs(v) < 10) ? D : 0;
						v = v / 2 + 8 * (p = Math.random()) - 4;
						w = -11 - 1 * p;
						y = Y;
						t.play();
					} else {
						y = H + 10;
						v = w = r = S = 0;
					}
				}
				
				// Hit test between the ball and the walls.
				v = (x < 0 | x > W - 28) ? -v : v;
				
				// Accelerate the ball.
				w += r;
			}, 40);
			
			// Draw hearts
			// hh: horizontal position
			// yy: vertical position
			// z: visibility array
			// d: direction
			// st: starting position
			// en: ending position
			// cl: color
			function dh(hh, yy, z, d, st, en, cl) {
				xx = hh;
				for (i = 0; i < n; ++i) {
					// If visibility of ith heart is undefined, set it true.
					// Otherwise, check if the heart is hit by the ball.
					if (z[i] & x + 20 > xx + 5 & x + 8 < xx + 18 & y + 15 > yy + 5 & y + 5 < yy + 23) {
						z[i] = false;
						s.play();
					}
					
					// Draw the heart if visible.
					if (z[i] = z[i] != false) {
						f = false;
						a.font = "23px sans";
						a.fillStyle = cl;
						a.fillText("❤", xx, yy);
					}
					
					// Calculate next heart's position.
					xx += d * 25;
					if (d * xx > d * en + 25)
						xx += st - en - d * 50;
				}
				
				// Calculate new horizontal position.
				hh += d * (yy / 20 - 1);
				if (d * hh > d * en + 25)
					hh = st - d * 25;
				return hh;
			}