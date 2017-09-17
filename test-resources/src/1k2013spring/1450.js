c.width=c.height=512;

var land=[];
var landSize = 256;
var keyControl = [0,0,0,0];
var hour = 14; // hour in radians
var timeShift=0;
for (;land.push(0)<landSize*landSize;) {}

function createHeightmapAntFarm() {
	// dig the river valley
	for (var y=0;y<landSize;++y) for (var x=0;x<landSize;++x) {
		land[landSize*(y&(landSize-1))+(x&(landSize-1))]=Math.min(0,51-55*Math.cos(x/40-Math.sin(y/40)));
	}
	// then add the hills
	for (var t=0,n=5e5;--n;t=(13*t+2+9*Math.cos(t))&-1) {
		x+=[0,1,0,-1][t&3];
		y+=[0,1,0,-1][3-t&3];
		var w=3;
		for (var j=-2;j<3;++j) for (var i=-2;i<3;++i) {
			land[landSize*((y+j)&(landSize-1))+((x+i)&(landSize-1))]+=.01;//*Math.exp(-(j*j+i*i)/3/w);
		}
	}
}


/**
 * Returns the altitude of the heightmap at coordinates (x,y) wrapping over landSize
 * If negative altitude, returns 0 instead
 * @param x X-coordinate used modulo landSize
 * @param y Y-coordinate used modulo landSize
 * @return altitude, with a minimum of zero
 */
function landAt(x,y) {return Math.max(0,land[landSize*(y&(landSize-1))+(x&(landSize-1))]); }

/**
 * Returns a blended, shadowed rgb color
 * Alpha blending is performed between two colors (r1,g1,b1) and (r2,g2,b2) 
 * Then the resulting color is darkened accoring to the shadow parameter
 * @param r1 : Red component of color 1 in [0,1] (usually ground color)
 * @param g1 : Green component of color 1 in [0,1] 
 * @param b1 : Blue component of color 1 in [0,1]
 * @param r2 : Red component of color 2 in [0,255] (usually atmosphere-hazed horizon color)
 * @param g2 : Green component of color 2 in [0,255]
 * @param b2 : Blue component of color 2 in [0,255]
 * @param alpha : 0 for color 1, 1 for color 1 * color 2
 * @param shadow : 0 for black, 1 for original color
 * @return rgb as a string description for canvas color-inputting methods
 */
function colorBlend(r1, g1, b1, r2, g2, b2, shadow, alpha)
{
	var r = (r1*(1-alpha)+alpha)*shadow*r2;
	var g = (g1*(1-alpha)+alpha)*shadow*g2;
	var b = (b1*(1-alpha)+alpha)*shadow*b2;
	
	return "rgb("+Math.round(r)+","+Math.round(g)+","+Math.round(b)+")";
}

createHeightmapAntFarm();

var px=3,py=-115; // starting position
var altitude=5000;
var yaw=-1.57, speed=0, roll=0, pitch=0;

var xdx = 0, xdy=-1;

/*
 hour                0 (midnight) .5     1    1.5(dawn)   2   2.5    3 (noon)  3.5     4    4.5 (dusk) 5    5.5     6
 
 horizon R                0       0      0     110     220    220      220     220     220     110      0     0     0 
 horizon G                0       0      0       0      92    195      250     244     178      68      0     0     0
 horizon B               64      64     64     255     255    255      255     255      64      64     64    64    64
 horizon                 dark blue             blue           cyan    light cyan     orange   bright pink    dark blue
 
 zenith R                50      50     50      50      50     50       50      50      50      50     50    50    50
 zenith G                 0      14     53     115     177    216      230     216     177     115     53    14     0
 zenith B                64      64     64      64     255    255      255     255     255      64     64    64    64
 zenith                 dark purple   greenish gray    light blue    cyan      light blue      greenish gray  dark purple

 
                                                |--------------- sun showing ----------------|
 sun    R               255     255    255     255     255    255      255     255     255     255    255   255   255
 sun    G               100     100    100     100     200    238      255     238     200     100    100   100   100
 sun    B                 0       0      0       0     106    204      255     204     106       0      0     0     0 
 sun                     deep orange                 light yellow     white    light yellow     deep orange
 
 halo R                 127    140     164     191     226    242      255     242     226     191    164   140   127
 halo G                   0      0       0       0     106    204      255     204     106       0      0     0     0 
 halo B                  55     55      55      55     163    231      255     231     163      55     55    55    55
 halo                   dark purple  crimson  faded pink  light pink  white  light pink      crimson   dark purple
*/
function drawView() {

	// Draw sky gradient
	var sky = a.createLinearGradient(0,0-200*speed,0,256-200*speed); 
	var sa = Math.max(0,-Math.cos(hour));
	var horizonR = Math.round(220*Math.max(0,Math.min(1,Math.cos(hour-3)*2+.5)));
	var horizonG = Math.round(255*Math.max(0,Math.cos(hour-3.2)));
	var horizonB = Math.round(64+191*Math.max(0,Math.min(1,Math.cos(hour-2.5)*3)));
	var zenithR = 50; 
	var zenithG = Math.round(115+115*Math.cos(hour-3));
	var zenithB = Math.round(64+191*Math.max(0,Math.min(1,Math.cos(hour-3)*4)));
	var haloR = 255; //Math.round(191+64*Math.cos(hour-3));
	var haloG = Math.round(255*Math.max(0, Math.cos(hour-3)));
	var haloB = Math.round(55+200*Math.max(0, Math.cos(hour-3)));
	var horizonL = Math.max(0,Math.cos(hour-3));

	sky.addColorStop(0, "rgb("+zenithR+","+zenithG+","+zenithB+")");  
	sky.addColorStop(1, "rgb("+horizonR+","+horizonG+","+horizonB+")");  
	a.fillStyle=sky;
	a.fillRect(0,0,512,512);
	
	// draw the sun as a solid color with a halo inside a gradient
	var sunX = Math.sin(hour)*672+432*(yaw-1.57);
	var sunY = 256+384*Math.cos(hour)-191*speed+roll*sunX*30;
	var halo = a.createRadialGradient(256+sunX, sunY, 25, 256+sunX, sunY, 256);
	halo.addColorStop(0, "rgb(255,"+Math.round(100+155*Math.sqrt(sa))+","+Math.round(255*sa)+")");
	halo.addColorStop(.05, "rgba("+haloR+","+haloG+","+haloB+",128)");
	halo.addColorStop(1, "rgba("+haloR+","+haloG+","+haloB+",0)");
	
	a.fillStyle = halo;
	a.fillRect(0,0,512,512);
	
	// simplified raymarching for the landscape
	for (var p=-256; p<256; p+=8) {
		var topLine=512;
		for (var j=2; j<160; ++j) {
			var i=j*p/384;
			var x=Math.round(px+xdx*i-xdy*j),y=Math.round(py+xdy*i+xdx*j);
			var level= landAt(x,y);
			var q=256-200*speed+(1.5*altitude-700*level)/j+roll*p*30;
			if (q<topLine) {	// y-buffer implementation
				var groundR=0, groundG=.5, groundB=1; // water
				if (level>0) {
					groundR = groundB = level/20;
					groundG = .5+level/20;
				}
				
				var shade = .4-(landAt(x+1,y)-level)*Math.sin(hour);

				a.fillStyle=colorBlend(groundR, groundG, groundB, 240*horizonL, 240*horizonL, 64+176*horizonL, (shade>1?1:(shade>0?shade:0)), j/200);
				
				a.fillRect(256+p,q,8,1+topLine-q);
				topLine=q;
			}
		}
	}
}


frame=0; t0 = new Date();	// FPS measure only
setInterval( function () {
	speed=speed*.97+2*keyControl[2]-2*keyControl[0]; 
	yaw=Math.atan2(Math.sin(yaw),Math.cos(yaw))+roll;
	roll=roll/2+keyControl[1]-keyControl[3];
	xdx = Math.sin(yaw), xdy=-Math.cos(yaw);
	px-=speed*xdy; 
	py+=speed*xdx; 
	var targetAltitude=500*landAt(px,py)+500;
	altitude=.9*altitude+.1*targetAltitude;
	hour+=timeShift;
	drawView();
	
	var vx=Math.round(px-xdy*10),vy=Math.round(py+xdx*10);
	frame++;
	var frameRate= 1000*frame / (new Date() - t0);
	debug.innerHTML="pos="+Math.round(px)+","+Math.round(py)+" roll="+roll+" angle="+yaw+" speed="+speed+" looking at="+vx+","+vy+", hour="+hour+", frameRate="+frameRate;
}, 10);

/* In this version, press key "s" to start/stop time. Default is time stopped. */
onkeydown = function(t) {  if (t.keyCode==83) {timeShift=1/256-timeShift;} else { keyControl[t.keyCode&3]=.01; } }
onkeyup = function(t) {  keyControl[t.keyCode&3]=0;}