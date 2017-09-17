var c = document.getElementById('canvas'),
	a = c.getContext('2d');				

window.onload=function(){
	//setup
	document.body.style.backgroundColor = "#222222";
	
	var points = [],
		palette = [],
		w = c.width = window.innerWidth-4,
		h = c.height = window.innerHeight-4;

	a.translate(w/2,h/2);

	points.push({x:0, y:0, xd:0, yd:0, r:0, g:0, b:0});
	for(var i = 1, r = 255, g = 255, b = 255; i < 21; i++){
		r-=8;
		g+=4;
		b-=12;
		points.push({
			x:points[i-1].x+(Math.random()*(w/8)-(w/16)),
			y: -(h/21)*i,
			xd: (Math.random()*2-1)/1,
			yd: (Math.random()*2-1)/1,
			r: r,
			g: g,
			b: b
		});
	}
	
	//start
	window.setInterval(function(){
		//update points
		for(var p = 1; p < points.length; p++){
			points[p].y += points[p].yd;
			if(points[p].y > h || points[p].y < -h){
				points[p].yd = points[p].yd * -1;
			}
			points[p].x += points[p].xd;
			if(points[p].x > w || points[p].x < -w){
				points[p].xd = points[p].xd * -1;
			}
		}

		//clear canvas
		a.save();
			a.fillStyle = "rgba(0,0,0,0.02)";
			a.fillRect(-w/2,-h/2,w,h);
		a.restore();

		//draw
		a.save();
		a.globalCompositeOperation = "lighter";
			for(var p = points.length-1; p > 1; p--){
				for(var i = 0; i < 5; i++){
					a.beginPath();
					a.moveTo(points[p-1].x, points[p-1].y);
					a.bezierCurveTo(points[p-1].x, points[p-1].y, 0, 0, points[p].x, points[p].y);
					a.strokeStyle="rgba(" + points[p].r + "," + points[p].g + "," + points[p].b + ",0.075)";
					a.stroke();
					a.rotate(1.256637);
				}
			}
		a.restore();
		a.rotate(0.005);
	
	}, 17);

};