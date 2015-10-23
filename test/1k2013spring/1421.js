b.style.margin=0;
var removes;
var GreenSun=255;
var H,W,MW,MH;
var X,Y;
var sun ={r:60};
var planetMouse=null;
var points=[];
var mq=Math.sqrt;
var G=0.008;
var randomColor=function(){return '#'+ ((Math.random()*0xFFFFFF)+'').slice(-6);}
var np=function(){planetMouse.m=true;planets.push(planetMouse);planetMouse=null;}
var planets=[{r:9,x:0,y:0,d:0,l:9,m:true,c:randomColor(),re:false}];
var pr=[];
var alpha=0.33;

function pw(n){
	return Math.pow(n,2)
}

//onmouseup event that puts a new planet in the stack
c.onmouseup=function(e){
   X=e.clientX;
    Y=e.clientY;
   planetMouse={x:0,y:0,r:10,d:Math.atan2(Y-MH,X-MW),m:false,l:mq(pw(X-MW)+pw(Y-MH))/12,c:randomColor(),re:false};
   np();
   //60 limit planets
   if(planets.length>=60)planets.shift();
};

//function that draws the star (SUN)
var createSun = function(color, r,H,W) {
	//draw 3 layers
	for(var i=1;i<=4;i++){	    
	    a.beginPath();
		a.fillStyle = 'rgba(255,'+GreenSun+',0,'+alpha*i+')';
		a.arc(W/2,H/2, r/(alpha*3*i), 0, 2 * Math.PI, true);
	    a.closePath();
	    a.fill();
	}
}

//function that draws each of the planets
var drawPlanet = function(planet){
	a.beginPath();
	a.fillStyle = planet.c;
	
	// get the position of the planet from the angle and distance
	// if we change the size of the window positions and sizes are kept
	planet.x = MW + Math.cos(planet.d)*planet.l*12;
	planet.y = MH + Math.sin(planet.d)*planet.l*12;
	planet.d+=planet.m?((G*sun.r)/planet.l):0;
	planet.nextx = MW + Math.cos(planet.d)*planet.l*12;
	planet.nexty = MH + Math.sin(planet.d)*planet.l*12;
	
	if(planet.r>0){
		a.arc(planet.x,planet.y, planet.r, 0, 2 * Math.PI, true);
	}
	a.fill();
}

setInterval(function(){
	draw();
},40);

// detect collisions between planets and star
// to follow these rules
// Subtract star size planets
// The remaining larger size planets to smaller planets
// The planets with a radius less than or equal to 0 are added to the pile of elimination
function colision(){
	var planet;
	var cPlanet;
	for(var i=0;i < planets.length;i++){
		planet=planets[i];
		if(detect(sun,planet)){
			//modify the size and color of the star
			planet.r--;
			sun.r+=.1;
			GreenSun--;
			if(planet.r<=0){
				pr.push(i);
			}
		}	
		for(var j=i+1;j<planets.length;j++){
			cPlanet=planets[j];
			if(detect(planet,cPlanet)){
				if(planet.r>=cPlanet.r){
					planet.r++;
					cPlanet.r--;
					if(cPlanet.r<=0){
						pr.push(j);
					}
				}else{
					planet.r--;
					cPlanet.r++;
					if(planet.r<=0){
						pr.push(i);
					}
				}
			}
		}
	}
	
	//walk the array and store the planets with a radius greater than 0
	removes=[];
	for(var i=0;i<planets.length;i++){
		if(planets[i].r>0){
			removes.push(planets[i]);
		}
	}
	planets=removes;
}

//funcion que detecta la colision entre dos cuerpos
function detect(planet1,planet2){
	var dx = planet1.nextx - planet2.nextx;
	var dy = planet1.nexty - planet2.nexty;
	var distance =(dx*dx + dy*dy);
	return(distance <= (planet1.r+planet2.r)*(planet1.r+planet2.r));
}

function draw(){
	// canvas conforms to the size of the window
	W = b.clientWidth;
	H = window.innerHeight-5;
	MW=sun.nextx=W/2;
	MH=sun.nexty=H/2;
	c.width=W;
	c.height=H;
	
	a.fillRect(0,0,W,H);
	a.fillStyle   = '#000';

	createSun("rgb(255,"+GreenSun+",0)",sun.r,H,W);
	colision();
	for(var i=0;i<planets.length;i++){
		drawPlanet(planets[i]);
	}
	
}