d = document.createElement("canvas");
		d.width = d.height = 5000;
		e = d.getContext("2d");
		
		sqrt2 = 1.41421;
		
		x = y = 2500;
		rot = xmx = ymx = 0;
		xmn = ymn = Infinity;
		
		function fractal(size, split, d, angle, col){
			if(split == 0){
				e.strokeStyle = col;
				e.beginPath();
				e.moveTo(x, y);
				xmx = Math.max(xmx, x += Math.cos(angle)*size);
				xmn = Math.min(xmn, x);				
				ymx = Math.max(ymx, y -= Math.sin(angle)*size);
				ymn = Math.min(ymn, y);
				e.lineTo(x, y);
				e.stroke();
			}else{
				angle += d*Math.PI/4;
				fractal(size/sqrt2, split-1, 1, angle, col);
				angle -= d*Math.PI/2;
				fractal(size/sqrt2, split-1, -1, angle, col);
				angle += d*Math.PI/4;
			}
		}
		
		function intro(){
			c.fillRect(0, 0, a.width, a.height);
			c.fillStyle = "white";
			c.fillText("Please wait.", a.width / 2, a.height / 2);
			c.fillStyle = "black";
			requestAnimationFrame(generate);
		}
		
		function generate(){
			x = y = 2500;
			fractal(1000, 17, 1, 0, "red");
			x = y = 2500;
			fractal(1000, 17, 1, Math.PI/2, "blue");
			x = y = 2500;
			fractal(1000, 17, 1, Math.PI, "green");
			x = y = 2500;
			fractal(1000, 17, 1, Math.PI * 3/2, "magenta");
			requestAnimationFrame(draw);
		}
		
		function draw(){
			c.fillRect(0, 0, 10000, 10000);
			c.save()
				c.translate(a.width / 2, a.height / 2);
				c.scale(0.6, 0.6);
				c.rotate(rot += 0.03);
				c.drawImage(d, xmn, ymn, xmx - xmn, ymx - ymn, -a.width / 2, -a.height / 2, a.width, a.height);
			c.restore()
			requestAnimationFrame(draw);
		}
		requestAnimationFrame(intro);