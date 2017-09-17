//setting canvas dimensions
c.width = c.height = 400;
//click event listener
c.addEventListener('click', e, false);
//array of blood
r = [];
//heart position
p = 200;
o = 100;
//size of heart outline
u = 2;
//has the heart been shot?
g = false;
//alpha for "WHY?" text
ga = 0;

//function to draw a blood streak
d = function(x,y,l,w) {
	a.strokeStyle = "#ff1000";
	a.lineCap = "round";
	a.lineWidth = w;
	a.beginPath();
	a.moveTo(x,y);
	a.lineTo(x,y+l);
	a.stroke();
}

//click event listener
function e(ev) {
    x = ev.clientX - c.offsetLeft;
    y = ev.clientY - c.offsetTop;
    f = a.getImageData(x, y, 1, 1).data;
    if(f[0]==255 && f[1] == 32) {
		r.push([x,y,0,1 + Math.random() * 3]);
		u = 5;
		g = true;
	}
}

//main loop
l = function(){
	//clear canvas
	a.rect(0, 0, 400, 400);
    a.fillStyle = "#DEDEDE";
    a.fill();
	
	//loop through all shot locations, and draw blood streaks.
	for(i = 0; i < r.length;i++) {
		d(r[i][0], r[i][1], r[i][2], r[i][3]);
		r[i][2] += Math.random() * 1.5;
	}
	
	//drawing the heart
	a.strokeStyle = "#222222";
	a.lineWidth = u;
	a.fillStyle = "#ff2000";
	a.beginPath();
	a.moveTo(p - 48, o);
	a.lineTo(p, o + 48);
	a.lineTo(p + 48, o);
	a.arc(p + 24, o - 24, 33.6, 0.25 * Math.PI, 1.25 * Math.PI, true);
	a.arc(p - 24, o - 24, 33.6, 1.75 * Math.PI, 0.75 * Math.PI, true);
	a.closePath();
	a.fill();
	a.stroke();
	
	//"Don't shoot..." text
	a.strokeStyle = "black";
	a.lineWidth = 0.8;
    a.strokeText("Don't shoot...", p - 32, o - 70);
	
	//if shot
	if(g) {
		a.strokeStyle = "#222222";
		a.globalAlpha = ga;
		a.strokeText("WHY?", p - 15, o - 10);
		ga+=0.002;
		a.globalAlpha = 1;
	}
	
	//heart outline
	if(u > 2) {u-=0.5;}
	
	//loop again!
	setTimeout(l, 1000 / 50);  
}
l();