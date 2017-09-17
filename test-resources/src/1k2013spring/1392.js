// variables for optimization -> closure compiler will remove this and use numeric value (which is longer)
var K1 = 1024;
var K2 = 2048;
var K4 = 4096;

// black background
b.style.background = "#000";

// width/height (get it and set canvas)
var WW=c.width=window.innerWidth-9;
var HH=c.height=window.innerHeight-9;

// limit size to 1024x512 - for speed and lookup table size limitation
var W = Math.min(WW, K1);
var H = Math.min(HH, 512);

// align center
var offsetX = (WW-W)/2;
var offsetY = (HH-H)/2;

// this is a polar angle conversion table, nothing else -> angle for X/Y, scaled to 0..1024 (1024 = PI/2)
// calculated for -2048..+2048
var lookuptable = new Int16Array(K4*K4);

for (var y = 0; y < K2; y++) {
	for (var x = 0; x < K2; x++) {
		var angle = x == 0 ? Math.PI/2 : Math.atan(y/x);
		// x>0, y>0
		lookuptable[(y+K2)*K4+(x+K2)] = angle*K1/Math.PI/2;
		// x<0, y>0
		lookuptable[(y+K2)*K4+(K2-x)] = K2-angle*K1/Math.PI/2;
		// x<0, y<0
		lookuptable[(K2-y)*K4+(K2-x)] = K2+angle*K1/Math.PI/2;
		// x>0, y<0
		lookuptable[(K2-y)*K4+(x+K2)] = K4-angle*K1/Math.PI/2;
	}
}

// secondary lookup is the flower effect, for 0..4096 (0..2PI)
var flowerScale = new Float64Array(K4);
for (var angle = 0; angle < K4; angle++) {
	flowerScale[angle] = 0.5-Math.abs(Math.sin(angle*Math.PI/128));
}

// time
var t = 0;

// we are setting up the buffers here
// buf is the real data storage, buf8 is used for copying to canvas, data32 is used for manipulation
var buf = new ArrayBuffer(W*H*4);
var buf8 = new Uint8ClampedArray(buf);
var data32 = new Uint32Array(buf);
var imageData = a.createImageData(W, H);

// this is a tricky one, working with Number was very slow, so I'm converting to int using
// Int32Array elements (this is used for lookup table access)
var intConversion = new Int32Array(2);

// the rendering function
function render(){

	// calculate zoom value based on time
	var zoom = 1+0.5*Math.cos(t/180);

	// distorsion scale - for the flower effect (0: standard checkerboard, 1: flower)
	// var distorsionScale = Math.sin(t/52)*2-1;
	// if (distorsionScale < 0) distorsionScale = 0;

	var distorsionScale = 0;
	var tX = t % 600;
	if (tX > 500) distorsionScale = Math.cos((tX-500)/64);  	// ramp out
	else if (tX > 300) distorsionScale = 1;				// in effect
	else if (tX > 200) distorsionScale = Math.sin((tX-200)/64)	// ramp in

	// center (offset is calculated from this)
	var cx = (0.5+Math.sin(t/120)/4)*W;
	var cy = (0.5+Math.sin(t/60)/4)*H;

	// amplification for noise
	var noiseAmp = Math.sin(t/114)*20-10;
	if (t < 900 || noiseAmp < 0) noiseAmp = 0;

	var _c = Math.cos(t/400.0)/zoom;
	var _s = Math.sin(t/400.0)/zoom;

	var dx = [ _c, _s ];
	var dy = [ -_s, _c ];

	// displacement of checkerboard relative to the flower
	var fcx = Math.cos(t/100)*192;
	var fcy = Math.sin(t/100)*192;

	var o = 0;
	var xx = -cx*dx[0]-cy*dy[0];
	var yy = -cx*dx[1]-cy*dy[1];
	for (cy = H; cy--; xx+=dy[0],yy+=dy[1]) {
		var noise = (Math.random()-0.5)*noiseAmp;
		var x = xx + noise * dx[0];
		var y = yy + noise * dy[0];
		for (cx = W; cx--; x+=dx[0],y+=dx[1],o++) {
			intConversion[0] = x+K2;
			intConversion[1] = y+K2;
			var angle = lookuptable[(intConversion[1]<<12)+intConversion[0]]; 
			var distorsion = flowerScale[(angle+t)&4095] * distorsionScale;
			var xr = x+x*distorsion + fcx;
			var yr = y+y*distorsion + fcy;
			var pixset = (xr^yr)&128;
			data32[o] = ((data32[o]&0xffffff)>>1) + (pixset ? 0xff800000: 0xff000000);
		}
	}

	imageData.data.set(buf8);
	a.putImageData(imageData, offsetX, offsetY);

	// increase "time"
	t++;

};

// testing: render single frame
// render();

// go go go
setInterval(render,30);