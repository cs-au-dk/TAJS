( function() {
	var c_sp = {
			x : 0,
			y : 0
		},
		z = a,
		t,
		L="♥",
		HH=45,
		CW = 1400,
		gameon = 1,
		CH = 780,
		speed = 1000,
		heartspeed = 2000,
		h_i=[],
		R=Math.random,
		heart_decrease=800,
		score = 0,
		p2 = Math.PI * 2,
		s_fs=12;
	h_i.length = CW;
	function clearCanvas() {
		z.fillStyle = "black";
		z.fillRect(0, 0, CW, CH);
	}
	function displayScore() {
		z.fillStyle = "black";
		z.fillRect(0, 0, 200, 50);
		z.fillStyle = "white";
		z.font="24px a",
		z.fillText("score: "+score,10,30);
	}
	function drawHeart() {
		if (!gameon) {
			setTimeout(drawHeart,heartspeed);
			return;
		}
		heartspeed+=20;
		var x = Math.floor(R()*CW);
			y = Math.floor(R()*CH);
			x_r = x+HH,i=0;
		z.font="62px a";    
		z.fillStyle="white"; 
		z.fillText(L,x,y);
		z.fillStyle="red";
		z.font="56px a"; 
		z.fillText(L,x+1.5,y-2);
		y-=50;
		while (x<x_r) {
			h_i[x] = [y,y+70];
			x++;
		}
		setTimeout(drawHeart,heartspeed);
	}
	function paintcanvas(x, y) {
		z.beginPath();
		z.lineWidth = 2;
		z.strokeStyle = "red";
		z.arc(x, y, 30, 0, p2, true);
		z.stroke();
		
		if (h_i[x]) {
			if (y > h_i[x][0] && y < h_i[x][1]) {
				clearCanvas();
				speed+=heart_decrease;
				heart_decrease-=25;
				h_i=[];
				h_i.length = CW;
				score+=100;
			}
		}
		score++;
		displayScore();
		t = setTimeout(function() {
			c_sp.x = x;
			c_sp.y = y;
			z.strokeStyle = "blue";
			z.clearRect(x, y, 10, 10);
			z.beginPath();
			z.arc(x, y, 20, 0, p2, true);
			z.stroke();
			//clearTimeout(t);
			speed -= R()*5;
		}, speed);
		if (speed<1)alert("Score: "+score+"!"),gameon=0;
	}
	c.width = CW;
	c.height = CH;

	c.onmousemove = function(e) {
		if (gameon) paintcanvas(e.pageX, e.pageY);
	}
	clearCanvas();
	drawHeart();
}())