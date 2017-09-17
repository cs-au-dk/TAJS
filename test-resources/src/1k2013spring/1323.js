var c, a;

window.onload = function(){
	var points = [], mX = 0, mY = 0, mE = 6, w, h;

	c = document.getElementById('canvas')
	a = c.getContext('2d');				
	w = c.width = window.innerWidth;
	h = c.height = window.innerHeight;
		
	function move(){
		for(var p in points){
			var v1m = points[p].x - mX,
				v2m = points[p].y - mY,
				vDm = Math.sqrt(v1m*v1m + v2m*v2m),
				vAm = Math.atan2(v2m, v1m);
				v1o = points[p].x - points[p].origX,
				v2o = points[p].y - points[p].origY,
				vDo = Math.sqrt(v1o*v1o + v2o*v2o),
				vAo = Math.atan2(v2o, v1o) + 0.78554;
					
			if(vDm < 200){
				points[p].x = points[p].x + (Math.cos(vAm) * (mE*20/vDm));
				points[p].y = points[p].y + (Math.sin(vAm) * (mE*20/vDm));
			} else {
				points[p].x = points[p].x - Math.cos(vAo)/(20/vDo);
				points[p].y = points[p].y - Math.sin(vAo)/(20/vDo);
			}
			
			var col = Math.round((vDo/200)*128);
			points[p].c = "rgba(0," + (0+col) + "," + (128-col) + ",1)";
		}
	}
	
	function draw(){
		a.save();
		a.globalCompositeOperation = "lighter";
		for(var p = 0; p < points.length; p++){
			a.fillStyle = points[p].c;
			a.fillRect(points[p].x, points[p].y, 6*2, 6*2);
		}
		a.restore();
	}
	
	function animate(){
		move();
		a.fillStyle="#112211";
		a.fillRect(0,0,w,h);
		draw();
	}
	
	var	hX = w/2 - 275,
		hY = h/2 - 110;
	for(var y = 1; y <= 20; y++){
		for(var x = 1; x <= 50; x++){
			var bX = (x * 5 * 2.2) + hX,
				bY = (y * 5 * 2.2) + hY;
			points.push({x:bX,y:bY,origX:bX,origY:bY,r:5,c:0});
		}
	}

	c.onmousemove = function(e){
		mX = e.pageX;
		mY = e.pageY;
	};
	c.onmousedown = function(){
		mE = 24;
	};
	c.onmouseup = function(){
		mE = 6;
	};
	window.setInterval(animate, 33);
}