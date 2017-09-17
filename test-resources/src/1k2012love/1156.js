// Computer Love
			c.width = 600;
			c.height = 600;
			a.fillStyle = '#fcfe53';
			a.fillRect(0, 0, 600, 600);
			d = [[77,56],[499, 39], [572, 56], [595, 400], [418, 570], [1, 537], [1, 440], [45, 393], [77, 56]];
			
			e = [[83, 63],[491, 50], [496, 356], [424, 318], [429, 111], [158, 111], [150, 308], [58, 343], [83, 63]];
			f = [[54, 388],[57, 350], [495, 364], [495, 404], [54, 388]];
			g = [[418, 569],[595, 400], [597, 423], [418, 569]];
			h = [[1, 537],[418, 569], [416, 536], [1, 510], [1, 537]];
			i = [[6, 491], [409, 515], [441, 466], [40, 427], [12, 450], [6, 491]];
			k = [e,f,g,h,i];
			function z(y, x) {
				a.beginPath();
				a.moveTo(y[0][0], y[0][1]);
				for (x = 1; x < y.length; x++) {
					a.lineTo(y[x][0], y[x][1]);
				}
				a.closePath();
				a.fill();
			}
			a.lineWidth = 2;
			a.strokeStyle = '#c2dbad';
			a.fillStyle = '#c2dbad';
			setTimeout("z(d);a.strokeStyle = '#4c4c4a';a.fillStyle = '#4c4c4a';y()", 500);
			function y() {
				for (j = 0; j < 5; j++) {
					setTimeout("z(k[" + j + "]);", (j + 1) * 1000);
				}
			}
			function x() {
				a.font = "18pt Helvetica";
				a.letterSpacing = "10px";
				a.fillText("K R A F T W E R K", 15, 30);
				a.fillText("C O M P U T E R   L O V E", 278, 30);
			}
			setTimeout(x, 6500);